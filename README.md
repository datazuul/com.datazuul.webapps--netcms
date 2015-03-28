DEVELOPMENT
===========
- create /var/log/netcms logging directory
$ sudo mkdir /var/log/netcms
$ sudo chmod 777 /var/log/netcms

- create /var/www/localhost/repository directory
$ sudo mkdir -p /var/www/localhost/repository
$ sudo chmod 777 /var/www/localhost/repository

- run with mvn jetty:run
- start with
http://localhost:8080
- Login (FIXME: it is static in SignInSession...): admin/admin123
