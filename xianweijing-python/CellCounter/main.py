import cv2
import numpy as np
import matplotlib.pyplot as plt
import argparse


def get_level_highlight(img):
    y_list, x_list, _ = plt.hist(img.ravel(), 256, [0, 256])
    highlight = 0
    y_max = max(y_list)
    # 寻找合适的色阶高光调整值
    for i in range(256):
        if y_list[i] < y_max * 0.009 and y_list[i] > y_max * 0.008:
            highlight = i
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


def count_cell(img_path):
    # 读取图像
    src = cv2.imread(img_path)
    # 预处理图像增强，去除薄雾等干扰
    src = pre_process(src)
    # 转换为灰度图像
    gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)
    # 二值化
    ret, binary = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)
    # 获取结构元素
    k = cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3))
    # 开操作
    binary = cv2.morphologyEx(binary, cv2.MORPH_OPEN, k)
    # 高斯模糊
    binary = cv2.GaussianBlur(binary, (11, 11), 3)
    # 轮廓发现
    contours, hierarchy = cv2.findContours(binary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    # 凸包个数
    count = 0
    # 绿色通道
    src_g = src[:, :, 1]
    # 红色通道
    src_r = src[:, :, 2]
    # 绿色个数
    count_green = 0
    # 红色个数
    count_red = 0
    for c in range(len(contours)):
        # 凸包检测
        points = cv2.convexHull(contours[c])
        total = len(points)
        # 凸包面积
        area = cv2.contourArea(points)
        count += 1
        # 绘制只有一个凸包的图像，用于统计凸包内像素值之和
        cimg = np.zeros_like(src_g)
        cv2.drawContours(cimg, contours, c, color=255, thickness=-1)
        # 像素值为255的点就是凸包内的点，获取了凸包内所有点的下标
        pts = np.where(cimg == 255)
        # 绿色像素均值
        green_avg = src_g[pts].sum() / area
        # 红色像素均值
        red_avg = src_r[pts].sum() / area
        # print('green:'+str(green_avg)+',red:'+str(red_avg))
        if green_avg > 1.5 * red_avg:
            # 绿色
            count_green += 1
            # 显示
            # for i in range(len(points)):
            #     x1, y1 = points[i % total][0]
            #     x2, y2 = points[(i + 1) % total][0]
            #     cv2.line(src, (x1, y1), (x2, y2), (0, 0, 255), 2, 8, 0)
        elif red_avg > 1.5 * green_avg:
            # 红色
            count_red += 1
            # 显示
            # for i in range(len(points)):
            #     x1, y1 = points[i % total][0]
            #     x2, y2 = points[(i + 1) % total][0]
            #     cv2.line(src, (x1, y1), (x2, y2), (0, 0, 255), 2, 8, 0)
        elif abs(green_avg - red_avg) < 30:
            # 红绿混合，各加一
            count_green += 1
            count_red += 1
            # 显示
            # for i in range(len(points)):
            #     x1, y1 = points[i % total][0]
            #     x2, y2 = points[(i + 1) % total][0]
            #     cv2.line(src, (x1, y1), (x2, y2), (0, 0, 255), 2, 8, 0)
    # 显示
    # cv2.namedWindow('contours_analysis', 0)
    # cv2.resizeWindow('contours_analysis', 700, 950)  # 自己设定窗口图片的大小
    # cv2.imshow("contours_analysis", src)
    # cv2.waitKey(0)
    # cv2.destroyAllWindows()
    return count_green, count_red


if __name__ == '__main__':
    argparser = argparse.ArgumentParser()
    argparser.add_argument('--img_path', type=str, help='待处理图片路径')
    args = argparser.parse_args()
    count_green, count_red = count_cell(args.img_path)
    print(count_green)
    print(count_red)
