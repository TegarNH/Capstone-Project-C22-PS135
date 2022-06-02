from flask import Flask
app = Flask(__name__)
@app.route('/hello/', methods=['GET', 'POST'])
def welcome():
    return "Hello World!"

@app.route('/ml/<string:kind_model>/<string:picture_path>', methods=['GET', 'POST'])
def ml(kind_model, picture_path):
    import sys
    import numpy as np
    from keras.models import load_model
    from keras.preprocessing import image
    import os
    import json
    import calendar
    import time

    model_path = ""
    if kind_model == "type":
        model_path = "./skin-type-model-80.h5"
    elif kind_model == "disease":
        model_path = "./skin-disease-model-75.h5"    

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
    return json_object, 200
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)   