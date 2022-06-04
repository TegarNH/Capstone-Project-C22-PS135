from operator import index
import sys
import numpy as np
from keras.models import load_model
from keras.preprocessing import image
import os
import json
import calendar
import time
import pandas as pd

kind_model = sys.argv[1] #pilihannya: type dan disease
model_path = ""
if kind_model == "type":
    model_path = "/content/drive/MyDrive/Model-ML/Model Final/skin-type-model-80.h5"
elif kind_model == "disease":
    model_path = "/content/drive/MyDrive/Model-ML/Model Final/skin-disease-model-75.h5"    
picture_path = sys.argv[2]

print(model_path, picture_path)
model = load_model(model_path)

img = image.load_img(picture_path, target_size=(150, 150))
x = image.img_to_array(img)
x /= 255
x = np.expand_dims(x, axis=0)
images = np.vstack([x])
classes = model.predict(images, batch_size=10)

print(classes[0])
value = max(classes[0])
print(value)
i = 0
index_value=0
for result in classes[0]:
    if result == value:
        index_value = i
    i += 1
skin_value = ""
if kind_model == "type":
    if index_value == 0:
        skin_value = "Dry"
    elif index_value == 1:
        skin_value = "Normal"
    elif index_value == 2:
        skin_value = "Oily"
    elif index_value == 3:
        skin_value = "Sensitive"
elif kind_model == "disease":
    if index_value == 0:
        skin_value = "Acne"
    elif index_value == 1:
        skin_value = "Black Spots"
    elif index_value == 2:
        skin_value = "Puff Eyes"
    elif index_value == 3:
        skin_value = "Wrinkles"
print(skin_value)
path_data_rekomendasi="data_rekomendasi.csv"
path_data_produk="data_produk.csv"
data_rekomendasi = pd.read_csv(path_data_rekomendasi)
data_produk = pd.read_csv(path_data_produk)
# type: 11 = dry, 12 = normal, 13 = acne, 14 = sensitive
# disease: 21 = acne, 22 = black spots, 23 = puff eyes, 24 = wrinkles
if skin_value == "Dry":
    hasil_rekomendasi = data_rekomendasi.query("Rekomendasi  == 11")
    hasil_produk = data_produk.query("Rekomendasi == 11")
elif skin_value == "Normal":
    hasil_rekomendasi = data_rekomendasi.query("Rekomendasi  == 12")
    hasil_produk = data_produk.query("Rekomendasi == 12")
elif skin_value == "Oily":
    hasil_rekomendasi = data_rekomendasi.query("Rekomendasi  == 13")
    hasil_produk = data_produk.query("Rekomendasi == 13")
elif skin_value == "Sensitive":
    hasil_rekomendasi = data_rekomendasi.query("Rekomendasi  == 14")
    hasil_produk = data_produk.query("Rekomendasi == 14")
elif skin_value == "Acne":
    hasil_rekomendasi = data_rekomendasi.query("Rekomendasi  == 21")
    hasil_produk = data_produk.query("Rekomendasi == 21")
elif skin_value == "Black Spots":
    hasil_rekomendasi = data_rekomendasi.query("Rekomendasi  == 22")
    hasil_produk = data_produk.query("Rekomendasi == 22")
elif skin_value == "Puff Eyes":
    hasil_rekomendasi = data_rekomendasi.query("Rekomendasi  == 23")
    hasil_produk = data_produk.query("Rekomendasi == 23")
elif skin_value == "Wrinkles":
    hasil_rekomendasi = data_rekomendasi.query("Rekomendasi  == 24")
    hasil_produk = data_produk.query("Rekomendasi == 24")    
#print(hasil_rekomendasi.iloc[0][1])
#print(hasil_produk.iloc[0][1])
#print(len(hasil_rekomendasi))
rekomendation_list = []
product_list = []
for value in range(len(hasil_rekomendasi)):
    rekomendation_list.append(hasil_rekomendasi.iloc[value][1])
    #print(rekomendation_list[value])
for value in range(len(hasil_produk)):
    product_list.append({"photo" : os.getcwd()+"/"+hasil_produk.iloc[value][2], "name" : hasil_produk.iloc[value][1], "linkProduct" : hasil_produk.iloc[value][3]})
    #print(product_list[value])

id_predict = calendar.timegm(time.gmtime())
dictionary = {"error" : "false", "message": "success", "id":id_predict, "resultDetection" : skin_value, "rekomendationList" : rekomendation_list, "productList" : product_list }
json_object = json.dumps(dictionary, indent=4)
print(json_object)
json_file = open(str(id_predict)+".json", "w")
json_file.write(json_object)
json_file.close()