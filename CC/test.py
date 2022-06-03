from distutils.log import debug
from flask import Flask, render_template, request, url_for, flash, redirect
app = Flask(__name__)
messages = [{'title': 'Message One',
             'content': 'Message One Content'},
            {'title': 'Message Two',
             'content': 'Message Two Content'}
            ]
@app.route('/hello/', methods=['GET', 'POST'])
def welcome():
    return "Hello World!"

@app.route('/ml/<string:kind_model>/<string:picture_path>', methods=['GET', 'POST'])
def ml(kind_model, picture_path):
    import model_reader

    return model_reader.model_reader(kind_model, picture_path),200
@app.route('/form-data', methods=['GET', 'POST'])
    return render_template("submit.html")

if __name__ == '__main__':
    app.run(debug=True)   