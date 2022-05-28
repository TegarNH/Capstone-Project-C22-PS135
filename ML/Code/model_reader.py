import sys
import numpy as np
from keras.models import load_model
from keras.preprocessing import image
import os
import json
import calendar
import time

kind_model = sys.argv[1]
model_path = ""
if kind_model == "type":
    model_path = "../Model/skin-type-model-80.h5"
elif kind_model == "disease":
    model_path = "../Model/skin-disease-model-75.h5"    
picture_path = sys.argv[2]

print(model_path, picture_path)
model = load_model(model_path)

img = image.load_img(picture_path, target_size=(150, 150))
x = image.img_to_array(img)
x /= 255
x = np.expand_dims(x, axis=0)
images = np.vstack([x])
classes = model.predict(images, batch_size=10)

print(classes)
value = max(classes)
index_value = classes.index(value)
skin_value = ""
if kind_model == "type":
    if index_value == 0:
        skin_value == "Dry"
    elif index_value == 1:
        skin_value == "Normal"
    elif index_value == 2:
        skin_value == "Oily"
    elif index_value == 3:
        skin_value == "Sensitive"
elif kind_model == "disease":
    if index_value == 0:
        skin_value == "Acne"
    elif index_value == 1:
        skin_value == "Black Spots"
    elif index_value == 2:
        skin_value == "Puff Eyes"
    elif index_value == 3:
        skin_value == "Wrinkles"

print(skin_value)

id_predict = calendar.timegm(time.gmtime())
dictionary = {"error" : "false", "message": "success", "id":id_predict, "resultDetection" : skin_value}
json_object = json.dumps(dictionary, indent=4)
json_file = open(str(id_predict)+".json", "w")
json_file.write(json_object)
json_file.close()
