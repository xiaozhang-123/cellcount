import json

import cv2
import numpy as np
from django.http import HttpResponse, HttpResponseBadRequest
from django.views.decorators.http import require_POST
from matplotlib import pyplot as plt

@require_POST
def test(request):
    result = {"status": "ok", "alive": 40, "dead": 40}
    return HttpResponse(json.dumps(result))

@require_POST
def cell_count_query(request):
    path = request.POST.get("path")
    if path is None:
        return HttpResponse(json.dumps({"status": "error"}))
    count_alive, count_dead = count_cell(path)
    return HttpResponse(json.dumps({"status": "ok", "alive": count_alive, "dead": count_dead}))

def get_level_highlight(img):
    y_list, x_list, _ = plt.hist(img.ravel(), 256, [0, 256])
    highlight = 0
    # plt.show()
    y_max = max(y_list)
    # 寻找合适的色阶高光调整值
    for i in range(256):
        if y_list[i] < y_max * 0.009 and y_list[i]>y_max*0.005:
            highlight = i
    if highlight>150 or highlight==0:
        highlight=150
    # print(highlight)
    return highlight


def adjust_image(img, input_shadows, input_highlights, midtones,
                 output_highlights=255, output_shadows=0):
    img = img.astype(np.float)

    # 输入色阶映射
    img = 255 * ((img - input_shadows) / (input_highlights - input_shadows))
    img[img < 0] = 0
    img[img > 255] = 255

    # 中间调处理
    img = 255 * np.power(img / 255.0, 1.0 / midtones)

    # 输出色阶映射
    img = (img / 255) * (output_highlights - output_shadows) + output_shadows
    img[img < 0] = 0
    img[img > 255] = 255

    img = img.astype(np.uint8)
    return img


def pre_process(img):
    return adjust_image(img, 0, get_level_highlight(img), 0.09)


def count_one_color(img,color,args):
    # 绿色的HSV范围
    lower_green = np.array([35, 43, 46])
    upper_green = np.array([77, 255, 255])
    # 红色的HSV范围
    lower_red = np.array([0, 43, 46])
    upper_red = np.array([10, 255, 255])
    if 'green'==color:
        lower=lower_green
        upper=upper_green
    elif 'red'==color:
        lower=lower_red
        upper=upper_red
    img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    mask = cv2.inRange(img, lower, upper)
    # 获取结构元素
    k = cv2.getStructuringElement(cv2.MORPH_CROSS, (5, 5))
    # 开操作
    mask = cv2.morphologyEx(mask, cv2.MORPH_OPEN, k)
    # 高斯模糊
    mask = cv2.GaussianBlur(mask, (11, 11), 3)
    # 寻找连通域
    contours, hierarchy = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    img=cv2.cvtColor(img,cv2.COLOR_HSV2BGR)
    if args.s:
        if not os.path.exists('./cache'):
            os.makedirs('./cache')
        basename=os.path.basename(args.img_path)[:-4]
        for c in range(len(contours)):
            # 凸包检测
            points = cv2.convexHull(contours[c])
            total = len(points)
            for i in range(len(points)):
                x1, y1 = points[i % total][0]
                x2, y2 = points[(i + 1) % total][0]
                cv2.line(img, (x1, y1), (x2, y2), (255, 255, 255), 2, 8, 0)
        cv2.imwrite('./cache/'+basename+'_'+color+'.jpg',img)
    return len(contours),img


def count_cell(args):
    # 读取图像
    src = cv2.imread(args.img_path)
    # 预处理图像增强，去除薄雾等干扰
    src = pre_process(src)

    count_green,marked_green = count_one_color(src, 'green',args)
    count_red,marked_red = count_one_color(src, 'red',args)
    return count_green, count_red


