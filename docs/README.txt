DEVELOPMENT:
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


Deployment:

- copy content backup to
# unzip -l /home/tpx1002/BACKUPS/backup-netcms-www.alexandria.de-20100418.zip /var/www/www.alexandria.de

- copy WAR file to:
# cp /home/tpx1002/APP-WARs/NetCMS-0.0.1-SNAPSHOT.war /var/lib/tomcat6/webapps/netcms.war

- mount in site configuration
# cd /etc/apache2/sites-available/
# nano www.alexandria.de
...
JkMount /netcms* ajp13_tomcat2_worker
...

- restart apache
# /etc/init.d/apache2 restart