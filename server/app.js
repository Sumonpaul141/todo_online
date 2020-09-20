var express = require("express");
const mongoose = require('mongoose');
var bodyParser 	= require("body-parser");
var app = express();
var Port =  process.env.Port || 5000;


mongoose.connect('mongodb://localhost:27017/newdb', { keepAlive: 1, useUnifiedTopology: true , useNewUrlParser: true }).then(() => console.log('MongoDB Connected...')) .catch(err => console.log(err));
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())


var userSchema = new mongoose.Schema({
	name: String,
    password : String,
    email : String,
});
var user = mongoose.model("user", userSchema);


app.post("/user_reg", function(req, res){

	var Sname= req.body.name;
    var Semail= req.body.email;
    var Spassword = req.body.password;
    
    if(Semail == null || Semail.trim() == "" ){

        var obj = {
            "code" : "0",
            "massage" : "Invalid parameter"
        }

        res.send(obj);
        console.log("Invalid phone!!");
    }else{
        var newUser = {name: Sname, password: Spassword, email : Semail};
        user.findOne({ 'email': Semail }, function(err, users){
            if (err) {
                console.log(err);
            }else{
                if(users == null){
                    user.create(newUser, function(err, newlyUser){
                        if (err) {
                            console.log(err);
                        }else{
                            var obj = {
                                "code" : "1",
                                "massage" : "User Created",
                                "name" : newlyUser.name,
                                "email" : newlyUser.email,
                                "id" : newlyUser.id
                            }
                            res.send(obj); 
                            console.log("User created!!");
                        }
                
                    });
                }else if (users != null){
                    var obj = {
                        "code" : "2",
                        "massage" : "user already exists"
                    }   
                    res.send(obj);
                    console.log("User exists in the database");
                }
            }
        })
    }
});

app.post("/user_login", function(req, res){

    var Semail= req.body.email;
    var Spassword = req.body.password;
    
    if(Semail == null || Semail.trim() == "" ){

        var obj = {
            "code" : "0",
            "massage" : "Invalid parameter"
        }

        res.send(obj);
        console.log("Invalid parameter");
    }else{
        user.findOne({ 'email': Semail , "password" : Spassword}, function(err, users){
            if (err) {
                console.log(err);
            }else{
                if(users == null){
                    var obj = {
                        "code" : "0",
                        "massage" : "User Not exists",
                    }
                    res.send(obj); 
                    console.log("User Not exists");
                }else if (users != null){
                    var obj = {
                        "code" : "2",
                        "massage" : "User details",
                        "name" : users.name,
                        "email" : users.email,
                        "id" : users.id,
                    }   
                    res.send(obj);
                    console.log("User Details");
                }
            }
        })
    }
});



app.get("/", function(req, res){
    res.send("this is the root page");
});


app.listen("5000",function(req, res){
    console.log("Your server is running on " + Port);
});