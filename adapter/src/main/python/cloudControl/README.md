Cloud4SOA cloudControl Adapter
==============================

This is the communication proxy between the cloud4SOA infrastructure and the PAAS cloudControl to manage applications.


Requirements
------------
This Project depends on:

- [GIT](http://git-scm.com/) ([Download page](http://git-scm.com/downloads))
- [Python 2.7+](http://www.python.org/download/releases/2.7/) ([Download page](http://www.python.org/download/releases/2.7/#download))
- [pip 1.2.1+](http://pypi.python.org/pypi/pip) ([Download page](http://pypi.python.org/pypi/pip#downloads))
- [virtualenv](http://pypi.python.org/pypi/virtualenv) ([Download page](http://pypi.python.org/pypi/virtualenv#downloads))


Install via your package manager, otherwise get the libraries.


How to install the adapter on cloudControl
------------------------------------------
1. Change into the directory

        $ cd cloudControl

(not anymore) 2. Define your cloudControl credentials

        $ vi c4sheroku.ini
        ~~~vi
        # ...
        cloudControl_user=i@mycompany.tld
        cloudControl_password=s3cr3t
        cloudControl_apikey=visit_heroku_to_get_your_api_key
        #...


        :wq
        ~~~
        
        This will be done via receiving the correct logon credentials by extracting the HTTP request header variable 'apiKey'
        
3. Commit your changes

        $ git commit -am "Modify heroku credentials to get access to the application management"

4. Login to heroku

        $ heroku login
        Enter your Heroku credentials.
        Email: <i@mycompany.tld>
        Password (typing will be hidden): 

5. Create a new heroku app

        $ heroku create <my_fancy_app>
        Creating <my_fancy_app>...

6. Push it to heroku

        $ git push heroku master

7. Open it

        $ heroku open


How to setup and extend this project
------------------------------------

1. Change into the directory:

        $ cd c4sheroku

2. Set up your virtual ENV
	
        $ mkvirtualenv <name_of_your_choice> .

3. Run pip:

        (name_of_your_choice)$ pip install -r requirements.txt

5. Start hacking :)
