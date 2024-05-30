document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const staffId = urlParams.get('id');
    const formTitle = document.getElementById('formTitle');
    const staffForm = document.getElementById('staffForm');
    const modal = document.getElementById('staffModal');
    const searchInput = document.getElementById('searchInput');
    const logoutBtn = document.getElementById('logoutBtn');
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const currentPageSpan = document.getElementById('currentPage');
    let currentStaffId = null;
    let currentPage = 0;
    const pageSize = 10;

    // Functions to get the JWT token from local storage
    function getAccessToken() {
        return localStorage.getItem('accessToken');
    }

    function saveAccessToken(token) {
        localStorage.setItem('accessToken', token);
    }

    function getRefreshToken() {
        return localStorage.getItem('refreshToken');
    }

    function saveRefreshToken(token) {
        localStorage.setItem('refreshToken', token);
    }

    function fetchWithToken(url, options) {
        options.headers = options.headers || {};
        options.headers['Authorization'] = 'Bearer ' + getAccessToken();

        return fetch(url, options).then(response => {
            if (response.status === 403) { // Token might be expired
                return refreshToken().then(newAccessToken => {
                    options.headers['Authorization'] = 'Bearer ' + newAccessToken;
                    return fetch(url, options);
                });
            }
            return response;
        });
    }

    function refreshToken() {
        const refreshToken = getRefreshToken();
        return fetch('/refresh-token', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({refreshToken})
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Failed to refresh token');
                }
            })
            .then(data => {
                saveAccessToken(data.accessToken);
                saveRefreshToken(data.accessToken)
                return data.accessToken;
            });
    }

    // Function to fetch and display staff members
    function fetchStaff(page, query = '') {
        const url = `http://localhost:8090/api/staff?page=${page}&size=${pageSize}&query=${encodeURIComponent(query)}`;
        fetchWithToken(url, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Failed to fetch staff members');
                }
            })
            .then(data => {
                const staffTable = document.getElementById('staffTable').getElementsByTagName('tbody')[0];
                staffTable.innerHTML = '';
                data.content.forEach(staff => {
                    const row = staffTable.insertRow();

                    const imgCell = row.insertCell(0);
                    if (staff.image && staff.image.name && staff.image.name !== "" && staff.image.data && staff.image.data.length > 0) {
                        const img = document.createElement('img');
                        img.src = `data:image/jpeg;base64,${staff.image.data}`;
                        img.alt = `${staff.firstName} ${staff.lastName}`;
                        img.style.width = '50px';
                        img.style.height = '50px';
                        imgCell.appendChild(img);
                        img.addEventListener('click', () => {
                            toggleImageSize(img);
                        });
                    }
                    row.insertCell(1).textContent = staff.firstName;
                    row.insertCell(2).textContent = staff.lastName;
                    row.insertCell(3).textContent = staff.email;
                    row.insertCell(4).textContent = staff.contactNumber;
                    row.insertCell(5).textContent = staff.department.name;
                    const actionsCell = row.insertCell(6);
                    const editButton = document.createElement('button');
                    editButton.textContent = 'Edit';
                    editButton.classList.add('btn', 'btn-edit');
                    editButton.addEventListener('click', () => {
                        openStaffModal(staff.id);
                    });
                    actionsCell.appendChild(editButton);

                    const deleteButton = document.createElement('button');
                    deleteButton.textContent = 'Delete';
                    deleteButton.classList.add('btn', 'btn-delete');
                    deleteButton.addEventListener('click', () => {
                        deleteStaff(staff.id);
                    });
                    actionsCell.appendChild(deleteButton);
                });
                updatePagination(data);
            })
            .catch(error => console.error('Error fetching staff members:', error));
    }

    function toggleImageSize(img) {
        if (img.style.width === '50px') {
            img.style.width = '200px';
            img.style.height = 'auto';
        } else {
            img.style.width = '50px';
            img.style.height = '50px';
        }
    }

    function updatePagination(data) {
        currentPageSpan.textContent = data.pageable.pageNumber + 1;
        prevPageBtn.disabled = data.pageable.pageNumber === 0;
        nextPageBtn.disabled = data.pageable.pageNumber + 1 === data.totalPages;
    }

    // Function to populate the departments dropdown
    function populateDepartments() {
        fetchWithToken('http://localhost:8090/api/department', {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Failed to fetch departments');
                }
            })
            .then(departments => {
                const departmentSelect = document.getElementById('departmentSelect');
                departmentSelect.innerHTML = '';
                departments.forEach(department => {
                    const option = document.createElement('option');
                    option.value = department.id;
                    option.text = department.name;
                    departmentSelect.add(option);
                });
                if (staffId) {
                    fetchStaffDetails(staffId);
                }
            })
            .catch(error => console.error('Error fetching departments:', error));
    }

    // Function to open the staff modal for creating or updating
    function openStaffModal(staffId) {
        currentStaffId = staffId;
        if (staffId) {
            fetchStaffDetails(staffId);
            formTitle.textContent = 'Update Staff Member';
        } else {
            formTitle.textContent = 'Create New Staff Member';
            staffForm.reset();
        }
        modal.style.display = 'block';
    }

    // Function to fetch staff details for updating
    function fetchStaffDetails(staffId) {
        fetchWithToken(`http://localhost:8090/api/staff/${staffId}`, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Failed to fetch staff details');
                }
            })
            .then(staff => {
                document.getElementById('firstName').value = staff.firstName;
                document.getElementById('lastName').value = staff.lastName;
                document.getElementById('email').value = staff.email;
                document.getElementById('contactNumber').value = staff.contactNumber;
                document.getElementById('departmentSelect').value = staff.department.id;
            })
            .catch(error => console.error('Error fetching staff details:', error));
    }

    // Function to handle form submission
    function handleFormSubmit(event) {
        event.preventDefault();

        const form = document.getElementById('staffForm');
        const formData = new FormData(form);

        const firstName = formData.get('firstName');
        const lastName = formData.get('lastName');
        const email = formData.get('email');
        const contactNumber = formData.get('contactNumber');
        const departmentId = formData.get('departmentId');
        const image = formData.get('image');
        const namePattern = /^[a-zA-Z]+$/;
        const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

        if (firstName.trim() === '') {
            alert('First Name is required.');
            return;
        }

        if (!namePattern.test(firstName)) {
            alert('First Name must contain only alphabet characters.');
            return;
        }

        if (lastName.trim() === '') {
            alert('Last Name is required.');
            return;
        }

        if (!namePattern.test(lastName)) {
            alert('Last Name must contain only alphabet characters.');
            return;
        }


        if (!emailPattern.test(email)) {
            alert('Please enter a valid email address.');
            return;
        }

        if (contactNumber.trim() === '') {
            alert('Contact Number is required.');
            return;
        }

        if (departmentId === '') {
            alert('Please select a department.');
            return;
        }

        if (!currentStaffId) {
            if (image && !['image/jpeg', 'image/png', 'image/gif'].includes(image.type)) {
                alert('Please upload a valid image file (jpg, png, gif).');
                return;
            }
        }

        const staffData = {
            firstName: firstName,
            lastName: lastName,
            email: email,
            contactNumber: contactNumber,
            departmentId: departmentId
        };

        formData.append('requestDto', new Blob([JSON.stringify(staffData)], {type: 'application/json'}));

        const url = currentStaffId ? `http://localhost:8090/api/staff/${currentStaffId}` : 'http://localhost:8090/api/staff';
        const method = currentStaffId ? 'PUT' : 'POST';

        fetchWithToken(url, {
            method: method,
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    alert(currentStaffId ? 'Staff member updated successfully!' : 'Staff member created successfully!');
                    form.reset();
                    modal.style.display = 'none';
                    fetchStaff(currentPage, searchInput.value);
                } else {
                    return response.json().then(error => {
                        throw new Error(error.message);
                    });
                }
            })
            .catch(error => console.error('Error creating/updating staff member:', error));
    }

    // Function to delete a staff member
    function deleteStaff(staffId) {
        if (confirm('Are you sure you want to delete this staff member?')) {
            fetchWithToken(`http://localhost:8090/api/staff/${staffId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert('Staff member deleted successfully!');
                        fetchStaff(currentPage, searchInput.value);
                    } else {
                        throw new Error('Failed to delete staff member');
                    }
                })
                .catch(error => console.error('Error deleting staff member:', error));
        }
    }

    // Function to log out the user
    function logout() {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login.html';
    }

    // Event listener to open the modal for creating a new staff member
    document.getElementById('createStaffBtn').addEventListener('click', () => {
        openStaffModal(null);
    });

    // Event listener to close the modal
    document.getElementsByClassName('close')[0].onclick = function () {
        modal.style.display = 'none';
    }

    // Close the modal if the user clicks outside of it
    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    }

    // Attach event listener to the form
    document.getElementById('staffForm').addEventListener('submit', handleFormSubmit);

    // Event listener for logout button
    logoutBtn.addEventListener('click', logout);

    // Event listener for search input
    searchInput.addEventListener('input', function () {
        currentPage = 0; // Reset to the first page on new search
        fetchStaff(currentPage, searchInput.value);
    });

    // Event listeners for pagination buttons
    prevPageBtn.addEventListener('click', function () {
        if (currentPage > 0) {
            currentPage--;
            fetchStaff(currentPage, searchInput.value);
        }
    });

    nextPageBtn.addEventListener('click', function () {
        currentPage++;
        fetchStaff(currentPage, searchInput.value);
    });

    // Fetch and display staff members on page load
    fetchStaff(currentPage);

    // Populate departments dropdown on page load
    populateDepartments();
});