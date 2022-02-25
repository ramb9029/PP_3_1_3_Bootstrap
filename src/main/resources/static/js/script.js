const usersTable = $('#getTable');
const userForm = $('#editModal');
const userAddForm = $('#addUser');

const roleList = [
    {id: 1, role: "ROLE_ADMIN"},
    {id: 2, role: "ROLE_USER"}
];

let checkAddRoles = () => {
    let array = [];
    let options = document.querySelector('#newRoles').options;
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            array.push(roleList[i])
        }
    }
    return array;
};

let checkEditRoles = () => {
    let array = [];
    let options = document.querySelector('#roles').options;
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            array.push(roleList[i])
        }
    }
    return array;
};

$('#usersTableLink').click(() => {
    switchUsersTable();
});

$('#userFormLink').click(() => {
    switchAddForm();
});

userAddForm.find(':submit').click(() => {
    saveUser();
});

function switchUsersTable() {
    $('#usersTableLink').addClass('active');
    $('#navUsersTable').addClass('show').addClass('active');
    $('#userFormLink').removeClass('active');
    $('#navUserForm').removeClass('show').removeClass('active');
    getAllUsers();
}

function initNavigation() {
    $('#adminAreaTab').click(() => {
        $('#adminAreaTab').addClass('active').removeClass('btn-light').addClass('btn-primary')
            .prop('aria-selected', true);
        $('#adminArea').addClass('active');
        $('#userAreaTab').removeClass('active').removeClass('btn-primary').addClass('btn-light')
            .prop('aria-selected', false);
        $('#userArea').removeClass('active');
    });

    $('#userAreaTab').click(() => {
        $('#userAreaTab').addClass('active').removeClass('btn-light').addClass('btn-primary')
            .prop('aria-selected', true);
        $('#userArea').addClass('active');
        $('#adminAreaTab').removeClass('active').removeClass('btn-primary').addClass('btn-light')
            .prop('aria-selected', false);
        $('#adminArea').removeClass('active');
    });
}

function switchAddForm() {
    $('#userFormLink').addClass('active');
    $('#navUserForm').addClass('show').addClass('active');
    $('#usersTableLink').removeClass('active');
    $('#navUsersTable').removeClass('show').removeClass('active');
    valueNewUser();
}

function getAllUsers() {
    fetch('/api/users').then(function (response) {
        if (response.ok) {
            response.json().then(users => {
                usersTable.empty();
                users.forEach(user => {
                    showUsers(user);
                });
            });
        }
    });
}

function showUsers(user) {
    let roleName = user.roles.map(role => " " + role.name.substr(5)); //!!

    usersTable.append($('<tr>').attr('id', 'userRow[' + user.id + ']')
        .append($('<td>').attr('id', 'userData[' + user.id + '][id]').text(user.id))
        .append($('<td>').attr('id', 'userData[' + user.id + '][name]').text(user.username))
        .append($('<td>').attr('id', 'userData[' + user.id + '][lastName]').text(user.lastName))
        .append($('<td>').attr('id', 'userData[' + user.id + '][email]').text(user.email))
        .append($('<td>').attr('id', 'userData[' + user.id + '][age]').text(user.age))
        .append($('<td>').attr('id', 'userData[' + user.id + '][roles]').text(roleName))
        .append($('<td>').append($('<button class="btn btn-info editButton" id="editModal">').click(() => {
            loadModalForm(user.id);
        }).text('Edit')))
        .append($('<td>').append($('<button class="btn btn-danger">').click(() => {
            loadModalForm(user.id, false);
        }).text('Delete')))
    );
}

function readonlyValue(value = true) {
    userForm.find('#name').prop('readonly', value);
    userForm.find('#lastName').prop('readonly', value);
    userForm.find('#email').prop('readonly', value);
    userForm.find('#age').prop('readonly', value);
    userForm.find('#password').prop('readonly', value);
    userForm.find('#roles').prop('disabled', value);
}

function updateUser(id) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json; charset=utf-8');

    let user = {
        'id': parseInt(userForm.find('#id').val()),
        'username': userForm.find('#name').val(),
        'lastName': userForm.find('#lastName').val(),
        'email': userForm.find('#email').val(),
        'age': userForm.find('#age').val(),
        'password': userForm.find('#password').val(),
        'roles': checkEditRoles()
    };

    let request = new Request('/api/users/', {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(user)
    });

    fetch(request).then(function (response) {
        response.json().then(function (userData) {
            userForm.modal('hide');

            $('#userData\\[' + userData.id + '\\]\\[name\\]').text(userData.username);
            $('#userData\\[' + userData.id + '\\]\\[lastName\\]').text(userData.lastName);
            $('#userData\\[' + userData.id + '\\]\\[email\\]').text(userData.email);
            $('#userData\\[' + userData.id + '\\]\\[age\\]').text(userData.age);
            $('#userData\\[' + userData.id + '\\]\\[password\\]').text(userData.password);
            $('#userData\\[' + userData.id + '\\]\\[roles\\]')
                .text(userData.roles.map(role => " " + role.name.substr(5))); //!
        });
    })
}

function loadModalForm(id, editMode = true) {
    fetch('/api/user/' + id, {method: 'GET'}).then(function (response) {
        response.json().then(function (user) {
            userForm.find('#id').val(id);
            userForm.find('#name').val(user.username);
            userForm.find('#lastName').val(user.lastName);
            userForm.find('#email').val(user.email);
            userForm.find('#age').val(user.age);
            userForm.find('#password').val(user.password);

            if (editMode) {
                userForm.find('.modal-title').text('Edit user');
                userForm.find('#password-div').show();
                userForm.find('.submit').text('Edit').removeClass('btn-danger').addClass('btn-primary')
                    .removeAttr('onClick')
                    .attr('onClick', 'updateUser(' + id + ');');
                readonlyValue(false);
            } else {
                userForm.find('.modal-title').text('Delete user');
                userForm.find('#password-div').hide();
                userForm.find('.submit').text('Delete').removeClass('btn-primary').addClass('btn-danger')
                    .removeAttr('onClick')
                    .attr('onClick', 'deleteUser(' + id + ');');
                readonlyValue();
            }
            //!!!!
            fetch('/api/roles').then(function (response) {
                if (response.ok) {
                    userForm.find('#roles').empty();
                    response.json().then(roleList => {
                        roleList.forEach(role => {
                            userForm.find('#roles')
                                .append($('<option>')
                                    .prop('selected', user.roles.filter(e => e.id === role.id).length)
                                    .val(role.id).text(role.name));
                        });
                    });
                }
            });
            userForm.modal();
        });
    })
}

function valueNewUser() {
    userAddForm.find('#newName').val('');
    userAddForm.find('#newLastName').val('');
    userAddForm.find('#newEmail').val('');
    userAddForm.find('#newAge').val('');
    userAddForm.find('#newPassword').val('');
}

function saveUser() {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json; charset=utf-8');

    let user = {
        'username': userAddForm.find('#newName').val(),
        'lastName': userAddForm.find('#newLastName').val(),
        'email': userAddForm.find('#newEmail').val(),
        'age': userAddForm.find('#newAge').val(),
        'password': userAddForm.find('#newPassword').val(),
        'roles': checkAddRoles()
    };

    let request = new Request('/api/users/', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(user)
    });

    fetch(request).then(function (response) {
        response.json().then(function (userData) {
            console.log(userData);

            switchUsersTable();
        });
    });
}

function deleteUser(id) {
    fetch('/api/user/' + id, {method: 'DELETE'}).then(function () {
        userForm.modal('hide');
        usersTable.find('#userRow\\[' + id + '\\]').remove();
    });
}

$(document).ready(
    () => {
        getAllUsers();
        initNavigation();
    }
);
