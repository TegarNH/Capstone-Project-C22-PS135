#from werkzeug import secure_filename
from distutils.log import debug
from email import message
from sre_constants import SUCCESS
from flask import Flask, render_template, request, url_for, flash, redirect, json, jsonify
from grpc import Status

app = Flask(__name__)

@app.route('/upload', methods=['GET', 'POST'])
def home():
    if request.method == 'POST':
        f = request.files['picture_path'] #get files from upload
        f.save(f.filename) #save file to server
        kind_model = request.form['kind_model']
        picture_path = f.filename #nama file picture yang diupload ke server
        import model_reader_data
        #json.dumps({'success':True}), 200, {'ContentType':'application/json'}
        return model_reader_data.model_reader_data(kind_model, picture_path)
    else:
        return render_template('index.html')
    

@app.route('/hello/', methods=['GET', 'POST'])
def welcome():
    return "Hello World!"

@app.route('/result/<string:id>', methods=['GET', 'POST'])
def ml(id):
    import json
    #kind_model = request.form("kind_model")
    id_predict = id # http localhost/result/id
    with open("result_json/"+str(id_predict)+".json") as json_file:
        json_object = json.load(json_file)
    return jsonify(json_object)

@app.route('/result', methods=['GET'])
def postdata():
    import json
    #kind_model = request.form("kind_model")
    id_predict = request.args.get("id") #url http /localhost/result?id=...
    with open("result_json/"+str(id_predict)+".json") as json_file:
        json_object = json.load(json_file)
    return jsonify(json_object)
if __name__ == '__main__':
    app.run(debug=True)