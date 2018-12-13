import tornado.web
import tornado.ioloop
import tornado.httpserver
from tornado.options import define, options
import json

define("port", default=9991, help="本地监听端口", type=int)
define("DEBUG", default=True, help="是否开启debug模式", type=bool)
define("TEST", default=False, help="测试服务器,支持跨域访问,推送测试模式", type=bool)
tornado.options.parse_command_line()

DEFAULT_TYPE = []

class BaseHandler(tornado.web.RequestHandler):

	@property
	def json_body(self):
		if not hasattr(self, '_json_body'):
			if hasattr(self.request, "body"):
				try:
					if not self.request.body:
						self._json_body = {}
					else:
						self._json_body = json.loads(self.request.body.decode('utf-8'))
				except ValueError:
					raise ArgsError("参数不是json格式！")
		return self._json_body
		
	def set_default_headers(self):
		self.set_header("Access-Control-Allow-Origin", "*")
		self.set_header("Access-Control-Allow-Headers", "x-requested-with")
		self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS')

	def get_argument(self, name, default=DEFAULT_TYPE, strip=True):
		if self.json_body:
			if name in self.json_body:
				rs = self.json_body[name]
				return rs
			elif default is DEFAULT_TYPE:
				raise MissingArgumentError(name)
			else:
				return default
		else:
			return super(BaseHandler, self).get_argument(name, default, strip)

	def finish_success(self, **kwargs):
		rs = {
			"status": "success",
			"result": list(kwargs.values())[0]
		}
		self.finish(json.dumps(rs))


class HintHandler(BaseHandler):
	async def post(self):
		matrix = self.get_argument("matrix")
		print(matrix)

		self.finish_success(result={"res": [1,1]})


class SaveHandler(BaseHandler):
	async def get(self):
		score = 0
		with open("score", "r") as f:
			score = int(f.readline())

		self.finish_success(result={"res": score})

	async def post(self):
		score = self.get_argument("score")
		with open("score", "w") as f:
			f.write(str(score))

		self.finish_success(result={"res": "ok"})


application = tornado.web.Application(
		handlers=[
			(r"/save", SaveHandler),
			(r"/hint", HintHandler)
		],
		TEST=options.TEST,
		debug=options.DEBUG,
		autoreload=True,
	)

if __name__ == '__main__':
	application.listen(options.port)
	ioloop = tornado.ioloop.IOLoop.current()
	ioloop.start()




# ################################
# import socket
# import sys

# # 创建 socket 对象
# serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 

# # 获取本地主机名
# host = "localhost"
# port = 9991
# # 绑定端口号
# serversocket.bind((host, port))
# # 设置最大连接数，超过后排队
# serversocket.listen(5)

# response = ""
# with open("response", "r") as f:
# 	response = f.read()

# while True:
# 	# 建立客户端连接
# 	clientsocket,addr = serversocket.accept()
	
# 	param = clientsocket.recv(1024).decode("utf8")

# 	if param[0] == 'G':
# 		print(response)
# 		clientsocket.send(response.encode("utf8"))
# 	else:

# 		option = param.split("=")[1]

# 		print(option)

# 		# if option == "1":
# 		# 	score = param.split("=")[3]
# 		# 	with open("score", "w") as f:
# 		# 		f.write(score)
# 		# else:
# 		# 	msg = ""
# 		# 	with open("score", "r") as f:
# 		# 		msg = f.readline()
# 		# 		print(msg)
# 		# 		clientsocket.send(msg.encode("utf8"))


