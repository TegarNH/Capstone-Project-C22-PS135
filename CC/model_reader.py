import sys
import numpy as np
from keras.models import load_model
from keras.preprocessing import image
import keras.utils
import keras
import keras.utils
from keras import utils as np_utils
import os
import json
import calendar
import time
from PIL import Image
from tensorflow.keras import optimizers
from keras import models



def model_reader(kind_model, picture_path):
    # kind_model = sys.argv[1] #masukin input jenis model
    model_path = ""
    if kind_model == "type":
        model_path = "skin-type-model-80.h5"
    elif kind_model == "disease":
        model_path = "skin-disease-model-75.h5"    
    # picture_path = sys.argv[2] # masukin input path gambar

    print(model_path, picture_path)
    model = load_model(model_path) #load model 

    #untuk atur file gambar
    img = image.load_img(picture_path, target_size=(150, 150))
    x = image.img_to_array(img)
    x /= 255
    x = np.expand_dims(x, axis=0)
    images = np.vstack([x])


    classes = model.predict(images, batch_size=10) #untuk memprediksi model berdasarkan gambar
    print(classes[0]) #array hasil
    value = max(classes[0]) #ambil hasil terbesar
    print(value)

    #ambil indeks berdasarkan hasil terbesar
    i = 0
    index_value=0
    for result in classes[0]:
        if result == value:
            index_value = i
        i += 1
    print(index_value)

    #mengeluarkan nilai dalam bentuk string berdasarkan indeks dengan nilai terbesar
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


    id_predict = calendar.timegm(time.gmtime()) #membuat id berdasarkan timestamp
    dictionary = {"error" : False, "message": "success", "id":str(id_predict), "type_detection":"skin "+kind_model+" detection", "resultDetection" : skin_value} #menentukan nilai dalam file .json
    json_object = json.dumps(dictionary, indent=4) #memasukkan dictionary ke bentuk json object
    json_file = open("result_json/"+str(id_predict)+".json", "w") #membuat file .json dan menentukan nama filenya
    json_file.write(json_object) #menulis json object ke file .json yang udah dibuat
    json_file.close() #menutup file
    return json_object