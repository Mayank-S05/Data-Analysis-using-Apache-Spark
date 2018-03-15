from flask import Flask , render_template , request , redirect , url_for
import sys
from threading import Thread
from operator import add
from flask_socketio import SocketIO, emit
from kafka import KafkaConsumer
import time
from pyspark import SparkContext
from gevent import monkey
monkey.patch_all()


app = Flask(__name__)
app.debug = True
#settings.py
import os
# __file__ refers to the file settings.py 
APP_ROOT = os.path.dirname(os.path.abspath(__file__))   # refers to application_top
print(APP_ROOT)
APP_STATIC = os.path.join(APP_ROOT, 'static')
app.config[ 'SECRET_KEY' ] = 'jsbcfsbfjefebw237u3gdbdc'
socketio = SocketIO( app )
thread = None




def background_stuff():
    """ Let's do it a bit cleaner """
    consumer = KafkaConsumer('test3',
                         group_id='my-group',
                         bootstrap_servers=['localhost:9092'] ,api_version=(0,10))
    while True:
    
        for message in consumer:
            socketio.emit('message', {'data': 'This is data', 'time': message.value}, namespace='/test')
            

@app.route('/')
def index():
    global thread
    if thread is None:
        thread = Thread(target=background_stuff)
        thread.start()
    return render_template('index.html')

@socketio.on('my event', namespace='/test')
def my_event(msg):
    print(msg['data'])

@socketio.on('connect', namespace='/test')
def test_connect():
    emit('my response', {'data': 'Connected', 'count': 0})


@socketio.on('disconnect', namespace='/test')
def test_disconnect():
    print('Client disconnected')
	
	


# @app.route("/", methods=["GET","POST"])
# def main():
	
# 	# if(request.method == "POST"):
# 	# 	_code = request.form['code']
# 	# 	return "yes"
# 	# 	# return redirect(url_for('getcode' , code = _code))
# 	# else:
# 	# with open(APP_ROOT+'/static/data.txt' , 'r') as f:
# 		# content = f.read()
# 		# return (f.read(), mimetype='text/plain')

	
# 	return render_template('index.html', content = "yes")


# def consume():
# 	consumer = KafkaConsumer('test2',
#                          group_id='my-group',
#                          bootstrap_servers=['localhost:9092'] ,api_version=(0,10))
# 	for message in consumer:
# 		yield(message.value)
# 		time.sleep(100)

# @socketio.on( 'my event' )
# def handle_my_custom_event( json ):
#   # print( 'recived my event: ' + str( json ) )

	

# 	socketio.emit( 'my response', consume() )
# 		# print(message.value)
# 		# return render_template('index.html' , content = message.value)
  

@app.route("/analyse", methods=["POST"])
def analyse():
	sc = SparkContext(appName="PythonWordCount")
	lines = sc.textFile(APP_ROOT+"/static/data.txt")
	counts = lines.flatMap(lambda x: x.split(' ')) \
	              .map(lambda x: (x, 1)) \
	              .reduceByKey(add)
	output = counts.collect()
	for (word, count) in output:
	    print("%s: %i" % (word, count))

	sc.stop()

	return render_template('index.html')

# if __name__ == "__main__":	
# 	app.run(host='127.0.0.1', debug=True, port=4000)

# if __name__ == '__main__':
# 	socketio.run( app, debug = True )


if __name__ == '__main__':
    socketio.run(app)
