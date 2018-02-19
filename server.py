from flask import Flask , render_template , request , redirect , url_for
app = Flask(__name__)
#settings.py
import os
# __file__ refers to the file settings.py 
APP_ROOT = os.path.dirname(os.path.abspath(__file__))   # refers to application_top
print(APP_ROOT)
APP_STATIC = os.path.join(APP_ROOT, 'static')


@app.route("/", methods=["GET","POST"])
def main():
	# if(request.method == "POST"):
	# 	_code = request.form['code']
	# 	return "yes"
	# 	# return redirect(url_for('getcode' , code = _code))
	# else:
	with open(APP_ROOT+'/static/data.txt' , 'r') as f:
		content = f.read()
		# return (f.read(), mimetype='text/plain')
		return render_template('index.html', var = content)

if __name__ == "__main__":	
	app.run(host='127.0.0.1', debug=True, port=4000)