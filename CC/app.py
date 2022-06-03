from flask import Flask, jsonify, request
app = Flask(__name__)

tipepredict = [{'kindmodel' : 'type'},{'kindmodel' : 'deases'}]

@app.route('/', methods=['GET'])
def hello():
  return jsonify({'message' : 'Hello World!'})

@app.route('/lang', methods=['GET'])
def returnALL():
  return jsonify({'tipepredict' : kindmodel})

@app.route('/lang/<string:kindmodel>', methods=['GET'])
def returnALL():
  langs = [tipepredicts for tipepredicts in tipepredict if tipepredict['kindmodel' == kindmodel]]
  return jsonify({'tipepredict' : langs[0]})

@app.route('/lang/', methods=['POST'])
def addOne():
  tipepredicts = {'kindmodel' : request.json['kindmodel']}
  tipepredict.append(tipepredicts)
  return jsonify({'tipepredict' : tipepredicts})

if __name__ == '__main__':
  app.run()