# 导入 socket、sys 模块
import socket
import sys

# 创建 socket 对象
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 

# 获取本地主机名
host = "localhost"

# 设置端口号
port = 9991

# 连接服务，指定主机和端口
s.connect((host, port))
s.send(b"hell")

# 接收小于 1024 字节的数据
msg = s.recv(1024)

s.close()

print(msg.decode("utf8"))