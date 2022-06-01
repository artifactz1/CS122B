import React, { useState, useEffect } from "react";
import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";
import { paymentOrder } from "backend/JSONPlaceHolder";
import { useUser } from "hook/User";
import CheckoutForm from "component/CheckoutForm";

// Make sure to call loadStripe outside of a component’s render to avoid
// recreating the Stripe object on every render.
// This is your test publishable API key.
const stripePromise = loadStripe("pk_test_51KxO1fGfd81zwjSPABaO5vA8xXKLdIuCTuqHErNuR6dy6l0i7asWPlagVLvJGCihWBfjzn62EEHyxGlgPbIpJ48200U4C9HTnm");

const Payment = () => {
    const [clientSecret, setClientSecret] = useState("");
    const [paymentIntentId, setPaymentIntentId] = useState("");
    const {accessToken} = useUser();

    useEffect(() => {
		paymentOrder(accessToken)
        .then((response) => {
            console.log("called payment");
			setClientSecret(response.data.clientSecret);
			// setPaymentIntentId(response.paymentIntentId);
            console.log("RESPONE: " + response);
		})
        .catch(() => {alert("clientSecret is empty ")});
	}, []);

    const appearance = {
        theme: 'stripe',
    };
    const options = {
        clientSecret,
        appearance,
    };

    return (
        <div>
            <h1>PAYMENT</h1>
        {clientSecret && (
            <Elements options={options} stripe={stripePromise} paymentIntentId={paymentIntentId}>
            <CheckoutForm />
            </Elements>
        )}
        </div>
    );
}

export default Payment;