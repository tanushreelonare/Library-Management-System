// COMMON UTILITIES
// Library Management System

const Validation = {

    emailPattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,

    mobilePattern: /^[0-9]{10}$/,

    imageTypes: [
        "image/jpeg",
        "image/jpg",
        "image/png"
    ]

};

// Validation Styling

function setError(input, errorElement, message){

    if(!input || !errorElement) return;

    input.classList.remove("input-valid");

    input.classList.add("input-invalid");

    errorElement.textContent = message;

}

function clearError(input, errorElement){

    if(!input || !errorElement) return;

    input.classList.remove("input-invalid");

    input.classList.add("input-valid");

    errorElement.textContent = "";

}

// Email Validation

function isValidEmail(email){

    return Validation.emailPattern.test(email);

}

// Mobile Validation

function isValidMobile(mobile){

    return Validation.mobilePattern.test(mobile);

}

// Allow Numbers Only

function numbersOnly(input,maxLength=10){

    input.value=input.value.replace(/\D/g,"");

    if(input.value.length>maxLength){

        input.value=input.value.substring(0,maxLength);

    }

}

// Letters Only

function lettersOnly(input){

    input.value=input.value.replace(/[^A-Za-z ]/g,"");

}

// Upper Case

function toUpperCase(input){

    input.value=input.value.toUpperCase();

}

// Show Password

function togglePassword(password,confirm,checkbox){

    const type=checkbox.checked?"text":"password";

    password.type=type;

    confirm.type=type;

}

// Validate Image

function validateImage(file){

    if(!file) return false;

    return Validation.imageTypes.includes(file.type);

}

// Preview Image

function previewSelectedImage(file,image){

    const reader=new FileReader();

    reader.onload=function(e){

        image.src=e.target.result;

    };

    reader.readAsDataURL(file);

}

// Modal

function openModal(modal){

    modal.style.display="block";

}

function closeModal(modal){

    modal.style.display="none";

}

// Clear Validation

function clearValidation(form){

    form.querySelectorAll(".error-message").forEach(error=>{

        error.textContent="";

    });

    form.querySelectorAll("input,select,textarea").forEach(input=>{

        input.classList.remove("input-valid");

        input.classList.remove("input-invalid");

    });

}