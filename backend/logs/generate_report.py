import xlsxwriter
import os
import dateutil.parser
from os import listdir
from os.path import isfile, join

directory = "."

dirs = [x[0] for x in os.walk(directory)]
dirs = [x[2:] for x in dirs]
dirs = [x for x in dirs if x]
print(dirs)

workbook = xlsxwriter.Workbook('report.xlsx')


class Image:
    def __init__(self, driver, timestamp, name):
        self.driver = driver
        self.timestamp = timestamp
        self.name = name
        pass



def generate_worksheet(name):
    worksheet = workbook.add_worksheet(name=name)
    images = []
    mypath = "./" + name
    onlyfiles = [f for f in listdir(mypath) if isfile(join(mypath, f))]
    for img in onlyfiles:
        if img.endswith(".png"):
            clean_img = img.replace(".png", "")
            driver, timestamp = clean_img.split("-", 1)
            driver = eval(driver)
            timestamp = dateutil.parser.parse(timestamp)
            images.append(Image(driver, timestamp, img))
    images = sorted(images, key=lambda x: x.timestamp)
    print([img.timestamp for img in images])
    worksheet.set_column(first_col=1, last_col=5, width=110)
    i = 0
    for image in images:
        worksheet.set_row(i, height=300)
        worksheet.write("A" + str(i), image.timestamp)
        fullname = "./" + name + "/" + image.name
        worksheet.insert_image(row=i, col=image.driver + 1, filename=fullname, options={'x_scale': 0.4, 'y_scale': 0.4})
        i += 1
    

for dir in dirs:
    generate_worksheet(dir)

workbook.close()