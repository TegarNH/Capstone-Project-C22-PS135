from distutils.log import debug
from sre_constants import SUCCESS
from flask import Flask, render_template, request, url_for, flash, redirect, json
app = Flask(__name__)

@app.route('/upload')
def home():
    return render_template('index.html')
    

@app.route('/hello/', methods=['GET', 'POST'])
def welcome():
    return "Hello World!"

@app.route('/ml/<string:kind_model>/<string:picture_path>', methods=['GET', 'POST'])
def ml(kind_model, picture_path):
    import model_reader

    return model_reader.model_reader(kind_model, picture_path),200
@app.route('/result', methods=['GET','POST'])
def postdata():
    kind_model = request.form['kind_model']
    picture_path = request.form['picture_path']
    import model_reader

    return model_reader.model_reader(kind_model, picture_path),200 //SUCCESS

if __name__ == '__main__':
    app.run(debug=True)   