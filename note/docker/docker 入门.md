# docker 入门

## 一、什么是docker?

docker是一种基于Goole公司推出的go语言实现的虚拟化技术，对进程进行封装隔离，属于操作系统层面的虚拟化技术，能够一定程度上替代传统虚拟机。由于隔离的进程独立于宿主和其他隔离的进程，因此也可以称其为容器。

docker 在容器的基础上，进行了进一步的封装，从文件系统、网络互联到进程隔离等等，极大的简化了容器的创建和维护。使得 Docker 技术比虚拟机技术更为轻便、快捷。

docker可以将你的开发环境、代码、配置文件等一并打包到这个容器中，并发和应用到任意平台中。

## 二、为什么要用docker

#### docker与虚拟机的对比

##### 1.更高的利用效率

由于容器不需要进行硬件虚拟以及运行完整操作系统等额外开销，Docker 对系统资源的利用率更高。无论是应用执行速度、内存损耗或者文件存储速度，都要比传统虚拟机技术更高效。因此，相比虚拟机技术，一个相同配置的主机，往往可以运行更多数量的应用。

##### 2.更快的启动时间

传统的虚拟机技术启动应用服务往往需要数分钟，而 Docker 容器应用，由于直接运行于宿主内核，无需启动完整的操作系统，因此可以做到秒级、甚至毫秒级的启动时间。大大的节约了开发、测试、部署的时间。

##### 3.一致的运行环境

开发过程中一个常见的问题是环境一致性问题。由于开发环境、测试环境、生产环境不一致，导致有些 bug 并未在开发过程中被发现。而 Docker 的镜像提供了除内核外完整的运行时环境，确保了应用运行环境一致性，从而不会再出现 「这段代码在我机器上没问题啊」 这类问题。

##### 4.持续交付和部署

对开发和运维（DevOps）人员来说，最希望的就是一次创建或配置，可以在任意地方正常运行。

使用 Docker 可以通过定制应用镜像来实现持续集成、持续交付、部署。开发人员可以通过 Dockerfile 来进行镜像构建，并结合 持续集成(Continuous Integration) 系统进行集成测试，而运维人员则可以直接在生产环境中快速部署该镜像，甚至结合 持续部署(Continuous Delivery/Deployment) 系统进行自动部署。

而且使用 Dockerfile 使镜像构建透明化，不仅仅开发团队可以理解应用运行环境，也方便运维团队理解应用运行所需条件，帮助更好的生产环境中部署该镜像。

##### 5.更轻松的迁移

由于 Docker 确保了执行环境的一致性，使得应用的迁移更加容易。Docker 可以在很多平台上运行，无论是物理机、虚拟机、公有云、私有云，甚至是笔记本，其运行结果是一致的。因此用户可以很轻易的将在一个平台上运行的应用，迁移到另一个平台上，而不用担心运行环境的变化导致应用无法正常运行的情况。

##### 6.更轻松的维护和扩展

Docker 使用的分层存储以及镜像的技术，使得应用重复部分的复用更为容易，也使得应用的维护更新更加简单，基于基础镜像进一步扩展镜像也变得非常简单。此外，Docker 团队同各个开源项目团队一起维护了一大批高质量的 官方镜像，既可以直接在生产环境使用，又可以作为基础进一步定制，大大的降低了应用服务的镜像制作成本。



## 三、docker的基本概念

1.镜像：类似于虚拟机中的镜像，是一个包含有文件系统的面向docker引擎的只读模板。任何应用程序的运行都需要环境，而镜像就是用来提供这种运行环境的。

2.容器：

3.仓库：

## 四、入门操作

#### 1.镜像操作

**搜索镜像**

```
docker search 镜像名
```

```shell
[C:\~]$ docker search nginx
NAME                               DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
nginx                              Official build of Nginx.                        14471               [OK]                
jwilder/nginx-proxy                Automated Nginx reverse proxy for docker con鈥?  1969                                    [OK]
richarvey/nginx-php-fpm            Container running Nginx + PHP-FPM capable of鈥?  808                                     [OK]
jc21/nginx-proxy-manager           Docker container for managing Nginx proxy ho鈥?  150                                     
linuxserver/nginx                  An Nginx container, brought to you by LinuxS鈥?  141                                     
tiangolo/nginx-rtmp                Docker image with Nginx using the nginx-rtmp鈥?  115                                     [OK]
                         
```

- NAME: 镜像仓库源的名称
- DESCRIPTION: 镜像的描述
- OFFICIAL: 是否 docker 官方发布
- stars: 类似 Github 里面的 star，表示点赞、喜欢的意思。
- AUTOMATED: 自动构建。

拉取镜像

```
docker pull 镜像名
```

```
[C:\~]$ docker pull nginx
Using default tag: latest
latest: Pulling from library/nginx
a076a628af6f: Pulling fs layer
0732ab25fa22: Pulling fs layer
d7f36f6fe38f: Pulling fs layer
f72584a26f32: Pulling fs layer
7125e4df9063: Pulling fs layer
f72584a26f32: Waiting
7125e4df9063: Waiting
d7f36f6fe38f: Verifying Checksum
d7f36f6fe38f: Download complete
f72584a26f32: Download complete
7125e4df9063: Verifying Checksum
7125e4df9063: Download complete
a076a628af6f: Verifying Checksum
a076a628af6f: Download complete
a076a628af6f: Pull complete
0732ab25fa22: Verifying Checksum
0732ab25fa22: Download complete
0732ab25fa22: Pull complete
d7f36f6fe38f: Pull complete
f72584a26f32: Pull complete
7125e4df9063: Pull complete
Digest: sha256:10b8cc432d56da8b61b070f4c7d2543a9ed17c2b23010b43af434fd40e2ca4aa
Status: Downloaded newer image for nginx:latest
docker.io/library/nginx:latest
```

默认拉取最新版本的

**查看所有的镜像**

```
[C:\~]$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
<none>              <none>              e78ac7dde700        5 days ago          294MB
<none>              <none>              9e9f23a6e2f0        5 days ago          75.7MB
nginx               latest              f6d0b4767a6c        6 weeks ago         133MB
node                12-alpine           0206ff8a5f9e        7 weeks ago         88.9MB
python              alpine              d4d4f50f871a        2 months ago        44.2MB
ubuntu              latest              f643c72bc252        3 months ago        72.9MB
hello-world         latest              bf756fb1ae65        13 months ago       13.3kB

```

参数 ： -a 查看中间镜像层

docker images -a 

**删除镜像**

```

[C:\~]$ docker rmi nginx
Untagged: nginx:latest
Untagged: nginx@sha256:10b8cc432d56da8b61b070f4c7d2543a9ed17c2b23010b43af434fd40e2ca4aa
Deleted: sha256:f6d0b4767a6c466c178bf718f99bea0d3742b26679081e52dbf8e0c7c4c42d74
Deleted: sha256:4dfe71c4470c5920135f00af483556b09911b72547113512d36dc29bfc5f7445
Deleted: sha256:3c90a0917c79b758d74b7040f62d17a7680cd14077f734330b1994a2985283b8
Deleted: sha256:a1c538085c6f891424160d8db120ea093d4dda393e94cd4713e3fff3c82299b5
Deleted: sha256:a3ee2510dcf02c980d7aff635909612006fd1662084d6225e52e769b984abeb5
Deleted: sha256:cb42413394c4059335228c137fe884ff3ab8946a014014309676c25e3ac86864

```

参数： -f 强制删除

清除虚悬镜像

```
docker image prune
```

虚悬镜像就是没有镜像名和标签的镜像，构建镜像失败、强制删除一个正在运行容器的镜像等操作都会产生，一般来说，虚悬镜像已经没有实际用处，可以随意删除

#### 2.容器操作

**启动容器**

```
docker run -it centos
```

参数说明：

- -i: 交互式操作。
- -t: 终端。
- centos: centos 镜像。

要退出终端，直接输入 exit:

```
exit
```

**查看容器**

查看所有容器及其运行状态

```
[C:\~]$ docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                        PORTS                NAMES
8b953b12004a        nginx               "/docker-entrypoint.鈥?   52 seconds ago      Up 50 seconds                 80/tcp               elastic_mendeleev
6bb2a75a1f2e        ubuntu              "/bin/bash"              2 minutes ago       Exited (0) 2 minutes ago                           great_hawking
da99d18a039f        ubuntu              "/bin/bash"              3 minutes ago       Exited (0) 2 minutes ago                           modest_mayer
bb8d037b0023        docker101tutorial   "/docker-entrypoint.鈥?   16 hours ago        Exited (255) 49 minutes ago   0.0.0.0:80->80/tcp   docker-tutorial
462ac1b73979        hello-world         "/hello"                 16 hours ago        Exited (0) 16 hours ago                            happy_antonelli
b050bb452672        hello-world         "/hello"                 17 hours ago        Exited (0) 17 hours ago                            sad_elbakyan
55b682f8284c        hello-world         "/hello"                 17 hours ago        Exited (0) 17 hours ago                            nifty_hoover
9fc800e431e4        ubuntu              "/bin/bash"              4 days ago          Exited (255) 3 days ago                            boring_rosalind
4d69693f6d64        ubuntu              "echo 'hello world'"     5 days ago          Exited (0) 4 days ago                              interesting_mayer

```

**停止一个容器**

```
docker stop 容器ID
docker restart 容器ID
```

**进入容器**

```
docker attach 容器ID

```

删除容器

```
docker rm 602237bcc6d6
docker rm -f 602237bcc6d6  #强制删除
docker container prune #清理所有处于终止状态的容器
```

3.运行web应用

(1) 拉取镜像

```
docker pull training/webapp
```

(2) 后台启动容器

```
[C:\~]$ docker run -d -P training/webapp python app.py
4e8fbfb5c712a6118c3b7b78cd35b9c4d54f52d766a0340dd26a3cbda8c10a45
```

-d:让容器在后台运行

-P：将容器内部使用的网络端口映射到宿主机

(3)查看容器运行状态

```
[C:\~]$ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                     NAMES
4e8fbfb5c712        training/webapp     "python app.py"          9 seconds ago       Up 8 seconds        0.0.0.0:32768->5000/tcp   practical_khorana
8b953b12004a        nginx               "/docker-entrypoint.鈥?   28 minutes ago      Up 9 minutes        80/tcp                    elastic_mendeleev

```

Docker 开放了 5000 端口（默认 Python Flask 端口）映射到主机端口 32768 上。

在本地浏览器请求：http://localhost:32768/，显示Hello world!

（4）查看web应用的日志

```
[C:\~]$ docker logs 4e8fbfb5c712
 * Running on http://0.0.0.0:5000/ (Press CTRL+C to quit)
172.17.0.1 - - [25/Feb/2021 02:37:31] "GET / HTTP/1.1" 200 -
172.17.0.1 - - [25/Feb/2021 02:37:31] "GET /favicon.ico HTTP/1.1" 404 -
```

参数： -f ：实时跟踪实时日志

--details 显示更多信息

--since string 显示自某个时间点之后的日志

```
docker logs --since 1m 4e8fbfb5c712 #查看最近1分钟之内的日志
172.17.0.1 - - [25/Feb/2021 03:00:45] "GET / HTTP/1.1" 200 -
docker logs -t --since="2021-02-25T03:00:00" 4e8fbfb5c712 #查看某时间点之后的日志
```

--tail string 从日志末尾显示多少行日志

--until string 显示自某个时间点之前的日志

(5)查看web应用程序容器的进程

```
docker top 970dc67458f6
```

(6) 检查web应用程序

查看docker的底层信息，返回一个json，记录了docker容器的配置和状态信息

```
docker inspect DockerId
```

（7）查询最后一次创建的容器

```
docker ps -l
```

