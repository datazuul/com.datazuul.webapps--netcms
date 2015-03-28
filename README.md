DEVELOPMENT
===========
- create /var/log/netcms logging directory
<pre><code>$ sudo mkdir /var/log/netcms
$ sudo chmod 777 /var/log/netcms
</code></pre>

- create /var/www/localhost/repository directory
<pre><code>$ sudo mkdir -p /var/www/localhost/repository
$ sudo chmod 777 /var/www/localhost/repository
</code></pre>
- run with mvn jetty:run
- start with
http://localhost:8080
- Login (FIXME: it is static in SignInSession...): admin/admin123
