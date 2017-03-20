var app = angular.module("EmployeeApp", ['ngRoute'])

//route mapping
app.config(function ($routeProvider) {
    $routeProvider
        .when("/login", {                   //login page
            templateUrl: "login.html"
        })
        .when("/main", {                    //main page
            resolve: {
                "check": function ($location) {     //checks if the user has logged in,
                    if (sessionStorage.getItem("Username") == null || sessionStorage.getItem("Username") == undefined) {
                        $location.path("/login");   //if not returns to login
                    }
                }
            },
            templateUrl: "main.html"

        })
        .otherwise({
            redirectTo: "/login"                    // returns any other page to 
        });
});

app.controller("myCtrl", myJsCtrl);

function myJsCtrl($http, $window, $location) {
    var self = this;
    self.myArray = [];                              //array of employees
    self.editArray = [];                            //array for edit fields
    self.newEmp = {};                               //new employee object
    self.editEmp = {};                              //employee object for the one being edited
    self.flags = [];                                //flags array to hold checked employees (by the checkbox)
    self.editMode = false;                          //if edit button is clicked
    self.flagged = false;                           //if there is a checkbox clicked

    self.failedResponse = false;                    //if there is a failed login
    self.successfulLogin = false;                   //if login is succesful



    self.checkInfo = function () {                                  //checks the user

        if (sessionStorage.getItem("Username") != undefined) {      //if user has logged in, go to main
            $location.path('/main');
        }
        else if (self.username != undefined) {                      //if user has entered login data, check it by webservice
            $http.get("http://localhost:8080/employeeManagement/webapi/admin", { params: { "username": self.username, "password": self.password } })
                .then(function (response) {
                    if (response.data == "false") {
                        self.failedResponse = true;                //either username or password is incorrect
                    }
                    else                                            //successful login
                    {
                        sessionStorage.setItem("Username", self.username);      //store username in localstorage
                        self.failedResponse = false;
                        self.successLogin = true;

                        self.main();                                    //start main function
                    }
                });
        }
    };

    self.login = function () {                                          //runs when the user is on login page
        self.checkInfo();


        if (sessionStorage.getItem("Username") == undefined) {          //the view is set to login
            $location.path("/login");
        }
    }

    self.main = function () {                                           //runs when the user has logged in

        $location.path("/main");                                        //the view is set to main

        $http.get("http://localhost:8080/employeeManagement/webapi/employees"   //request to fetch all employees
            , { dataType: "json" })
            .success(function (response, status) {
                self.myArray = response;                                //put employees to myArray
            });
    };
    self.addNew = function () {                                         //add a new employee 
        self.newEmp.firstName = self.newFirstName;
        self.newEmp.lastName = self.newLastName;
        self.newEmp.position = self.newPosition;
        var date = new Date();
        self.newEmp.id = date.toString();                               //the employee id is creation date as toString
        $http.post                                                      //post new employee to webservice
            ("http://localhost:8080/employeeManagement/webapi/employees"
            , self.newEmp)
            .success(function (response, status) {                      //clone employee, and push clone to the editArray 
                var temp = myClone(self.newEmp);                        //so that the array and the database are consistent
                self.myArray.push(temp);
            });
    };


    self.delete = function () {                                         //delete employees by delete button

        var deleteFailure = false

        for (var i = 0; i < self.flags.length; i++) {                   //send delete request to webservice
            if (self.flags[i] == true) {                                //for all the flagged employees
                $http.delete
                    ("http://localhost:8080/employeeManagementd/webapi/employees/" + self.myArray[i].id)
                    .error(function (response) {        //if deletion fails
                        self.main();
                        alert("deletion is unsuccessful due to failure to webservice connection");
                    });
            }
        }
        for (var i = 0; i < self.flags.length; i++) {       //update flags and employee arrays after deletions
            if (self.flags[i] == true) {
                self.flags.splice(i, 1);
                self.myArray.splice(i, 1);
                i--;
            }
        }
    }
    self.newReady = function () {                           //if all the fields for the new employee are filled
        if (self.newLastName && self.newFirstName && self.newPosition)
            return true;
        else
            return false;
    }

    self.checkBoxClicked = function () {                      //if any of the checkboxes is clicked
        if (self.flags.includes(true))
            self.flagged = true;
        else
            self.flagged = false;
    };

    self.editToggle = function () {                          //if the user is in edit mode (toggles between edit and cancel buttons)
        console.log("edit");
        self.editMode = !self.editMode;
        if (self.editMode) {
        }
    }
    self.cancelEdit = function () {                         //user cancels the edition
        for (var i = 0; i < self.flags.length; i++) {
            if (self.flags[i] == true) {                    //reset editfields to original values
                self.myArray[i].firstNameEdit = self.myArray[i].firstName;
                self.myArray[i].lastNameEdit = self.myArray[i].lastName;
                self.myArray[i].positionEdit = self.myArray[i].position;
                self.flags[i] = false;                      //all flags are set to false
            }
        }
        self.flagged = false;                               //no more flagged chekbxes
        self.editMode = false;
    }

    self.filter = function () {                            //filtering 
        if (!self.firstNameFilter)                          //if any of the fields is null or undefined set it to ""
            self.firstNameFilter = "";
        if (!self.lastNameFilter)
            self.lastNameFilter = "";
        if (!self.positionFilter)
            self.positionFilter = "";

        $http.get                                          //send query to webservice with chosen filters
            ("http://localhost:8080/employeeManagement/webapi/employees?firstname="
            + self.firstNameFilter + "&lastname=" + self.lastNameFilter
            + "&position=" + self.positionFilter)
            .success(function (response) {                  //if filtering is successful, update array
                self.myArray = response;

            });
    }
    self.sort = function (sorter, sortDir) {                //sorts the employees by sort type and sort direction(ascending-descending)
        if (!self.firstNameFilter)                          //if filtering options are null or undefined, set them to empty arrays
            self.firstNameFilter = "";
        if (!self.lastNameFilter)
            self.lastNameFilter = "";
        if (!self.positionFilter)
            self.positionFilter = "";
        //     self.sorter = sorter;                               
        //     self.sortDir = sortDir;
        $http.get                                           //make a get request with sort type and sort direction
            ("http://localhost:8080/employeeManagement/webapi/employees?firstname="
            + self.firstNameFilter + "&lastname=" + self.lastNameFilter
            + "&position=" + self.positionFilter + "&sort=" + sorter + "&sortdir=" + sortDir)
            .success(function (response) {
                self.myArray = response;                    //update employees array

            });
    }

    self.saveEdit = function () {                           //save the editions made
        for (var i = 0; i < self.flags.length; i++) {
            if (self.flags[i] == true) {                    //if the checkbox is checked
                if (self.myArray[i].firstNameEdit           //if any of the edit field is not touched, then
                    || self.myArray[i].lastNameEdit         //use the employee property instead
                    || self.myArray[i].positionEdit) {
                    self.myArray[i].firstNameEdit ?
                        self.editEmp.firstName = self.myArray[i].firstNameEdit :
                        self.editEmp.firstName = self.myArray[i].firstName;
                    self.myArray[i].lastNameEdit ?
                        self.editEmp.lastName = self.myArray[i].lastNameEdit :
                        self.editEmp.lastName = self.myArray[i].lastName;
                    self.myArray[i].positionEdit ?
                        self.editEmp.position = self.myArray[i].positionEdit :
                        self.editEmp.position = self.myArray[i].position;

                    $http.put("http://localhost:8080/employeeManagement/webapi/employees/" + self.myArray[i].id
                        , self.editEmp) //make updates by put requests
                        .error(function (response) {    //if an error occurs in put requests
                            self.main();
                            alert("update is unsuccessful due to failure to webservice connection");
                        });

                    self.myArray[i].firstName = self.editEmp.firstName; //update the employees array
                    self.myArray[i].lastName = self.editEmp.lastName;
                    self.myArray[i].position = self.editEmp.position;

                }
           /*     else {          
                    self.myArray[i].firstNameEdit = self.myArray[i].firstName;
                    self.myArray[i].lastNameEdit = self.myArray[i].lastName;
                    self.myArray[i].positionEdit = self.myArray[i].position;
                    self.flags[i] = false;
                }*/
                self.flags[i] = false;  //cancel the check flags
            }
        }
        self.editMode = false;      //cancel editmode
        self.flagged = false;       //no more flagged employe
    }

    self.logout = function () {     //logout from main

        sessionStorage.removeItem("Username");
        self.username = null;
        self.password = null;
        self.login();

    }


    function myClone(obj) {         //clone an object
        var copy = obj.constructor();
        console.log("obj" + obj);

        console.log("length" + Object.keys(obj).length);
        for (var i = 0; i < Object.keys(obj).length; i++) {
            copy[Object.keys(obj)[i]] = obj[Object.keys(obj)[i]];
        }
        return copy;
    }

    if ($location.path() == "login")        //run the app depending on the current route
        self.login();
    else
        self.main();
}
