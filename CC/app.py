from flask import Flask
app = Flask(__name__)


@app.route('/hello/', methods=['GET', 'POST'])
def welcome():
    return "Hello World!"


@app.route('/ml/<string:kind_model>/<string:picture_path>', methods=['GET', 'POST'])
def ml(kind_model, picture_path):
    import model_reader

    return model_reader.model_reader(kind_model, picture_path), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
root@healthlens-server: / home/healthlens_bucket1/upload
