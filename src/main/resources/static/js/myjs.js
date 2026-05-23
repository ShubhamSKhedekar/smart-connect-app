console.log("Hello Js is active")


//show password logic implementation
const passwordEle = document.getElementById("exampleInputPassword1");
const passwordIcon = document.getElementById("eye-icon");
//console.log("Password Icon: "+passwordIcon);
//console.log("passwordEle : "+passwordEle);
//queryselector is used to fetch class like getElementByClassName
if(passwordIcon){
	document.getElementById("eye-icon").addEventListener("click", function(){
	console.log("show password");
	if(passwordEle.type==="password"){
		passwordEle.type="text";
		//passwordIcon.classList.remove("fa-eye");
		//passwordIcon.classList.add("fa-eye-slash");
	}
	else{
		passwordEle.type="password";
		//passwordIcon.classList.remove("fa-eye-slash");
		//passwordIcon.classList.add("fa-eye");
	}
	});
}


//Sidebar closing opening logic on basis of sidebar visibiliy - used jquery
//below logic was not working 
/*function toggleSwitch(){
	console.log("in toggle switch");
	$(document).ready(function(){
		console.log("ready");
	})
	
	if($(".sidebar-panel").is(":visble")){
		console.log("visible");
		$(".sidebar-panel").css("display", "none");
		$("content").css("margin-left", "0%");
		$("content").css("width", "100%");
	}
	else{
		$('.sidebar-panel').css("display", "block");
		$('content').css("margin-left", "20%");
		$('content').css("width", "80%");
	}
}*/
//console.log("myjs is working");
$("#hide-sidebar").click(function(){
	//console.log("hide");
	$(".sidebar-panel").css("display", "none");
	$(".content").css("margin-left", "0%");
	$(".content").css("width", "100%");
});

$("#show-sidebar").click(function(){
	//console.log("show");
	$('.sidebar-panel').css("display", "block");
	$('.content').css("margin-left", "20%");
	$('.content').css("width", "80%");
});


//Start: Search contact logic implemented
function searchContacts(){
	console.log("searching");
	const query = document.getElementById("search-bar").value;

	if(query==""){
		$("#result-block").css("display", "none");
	}
	else{
		let url = "http://localhost:8080/user/searchcontacts/"+query;
		fetch(url)
			.then((response) =>{
				return response.json();
			})
			.then((data)=>{
				console.log(data);
				let displayContacts=`<div class="list-group result-div">`;
		
				data.forEach(c => {
					displayContacts+=`<div class="list-group-item">
					<a href='/user/${c.cId}/viewcontactprofile' class="text-decoration-none fs-5 text-center" >${c.name}</a>
					</div>`;
				});

				displayContacts+=`</div>`;
				document.getElementById("result-block").innerHTML = displayContacts;
			});

		$("#result-block").css("display", "block");
	}	
}
//End: Search contact logic implemented

//show password logic for password change process in settings.html
//for intial password as - it is having same feilds for eye icon and password1 - intial logic fpr passwrod chnage 
//works
//implementing for show new password 
const passwordEle2 = document.getElementById("exampleInputPassword2");
const passwordIcon2 = document.getElementById("eye-icon2");
//console.log("Password Icon: "+passwordIcon);
//console.log("passwordEle : "+passwordEle);
//queryselector is used to fetch class like getElementByClassName
if(passwordIcon2){
	document.getElementById("eye-icon2").addEventListener("click", function(){
	console.log("show password");
	if(passwordEle2.type==="password"){
		passwordEle2.type="text";
		//passwordIcon.classList.remove("fa-eye");
		//passwordIcon.classList.add("fa-eye-slash");
	}
	else{
		passwordEle2.type="password";
		//passwordIcon.classList.remove("fa-eye-slash");
		//passwordIcon.classList.add("fa-eye");
	}
	});
}

//Razorpay payment logic implementation
function intiatePayment(){

	var amount = $("#amount").val();
	//console.log(amount);

	if(amount==null || amount==""){
		console.log("Amount is blank!");
		return;
	}

	$.ajax({
		url:"/user/createorder",
		method:"post",
		data:JSON.stringify({
			amount:amount,
			info:'order_request'
		}),
		contentType:"application/json",
		dataType:"json",
		success: function(response){
			console.log(response);
			
			var options = {
				key: "rzp_test_jabxAwxeNFGlPC", // Enter the Key ID generated from the Dashboard
				amount: response.amount , // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
				currency: "INR",
				name: "Smart Contact Manager",
				description: "Donation",
				image: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWaLJT4tjvPx8wMpLsIYrhYoBMHpOk3X6CUw&s",
				order_id: response.id, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
				handler: function (response){
					/*alert(response.razorpay_payment_id);
					alert(response.razorpay_order_id);
					alert(response.razorpay_signature);
					alert("Payment success!");*/
					//console.log("payment successfull: We got payment order details!");

					swal({
						title: "Payment Done!",
						text: "Donated for right cause",
						icon: "success",
						button: "Okay",
					  });

					  updateOrder(response.razorpay_payment_id, response.razorpay_order_id, "success");
				},
				prefill: {
					name: "",
					email: "",
					contact: ""
				},
				notes: {
					address: "Razorpay Corporate Office"
				},
				theme: {
					color: "#3399cc"
				}
			};
			
			
			var rzp1 = new Razorpay(options);
			
			//on payment failure
			rzp1.on('payment.failed', function (response){
			        /*alert(response.error.code);
			        alert(response.error.description);
			        alert(response.error.source);
			        alert(response.error.step);
			        alert(response.error.reason);
			        alert(response.error.metadata.order_id);
			        alert(response.error.metadata.payment_id);*/
			        // alert("Payment failed!");
					swal("Payment failed!", "Something went worng, try again!", "error");
			});
			
			rzp1.open();
		},
		error:function(error){
			//console.log(error);
			swal("Payment failed!", "Something went worng, try again!", "error");
		}
	});
}

function updateOrder(paymentId, orderId, status){
	//console.log(paymentId+" "+orderId+" "+status);
	
	$.ajax({
		url:"/user/updateorder",
		method:"post",
		contentType:"application/json",
		data:JSON.stringify({
			paymentId:paymentId,
			orderId:orderId,
			status:status
		}),
		dataType:"json",
		success:function(response){
			console.log(response + " Data updated success");

		},
		error:function(error){
			console.log(error+" Data Update failed");
		}
	})

	return;
}