
import Config from "backend/config.json";
import Axios from "axios";



/**
 * We use axios to create REST calls to our backend
 *
 * We have provided the login rest call for your
 * reference to build other rest calls with.
 *
 * This is an async function. Which means calling this function requires that
 * you "chain" it with a .then() function call.
 * <br>
 * What this means is when the function is called it will essentially do it "in
 * another thread" and when the action is done being executed it will do
 * whatever the logic in your ".then()" function you chained to it
 * @example
 * login(request)
 * .then(response => alert(JSON.stringify(response.data, null, 2)));
 */
export async  function posts() {
    const options = {
        method: "GET", // Method type ("POST", "GET", "DELETE", ect)
        baseURL:Config.moviesURL, // Base URL (localhost:8081 for example)
        url: Config.movies, // Path of URL ("/login")
    }

    return Axios.request(options);
}

export async function post(id, accessToken) {
    const options = {
        method: "GET", // Method type ("POST", "GET", "DELETE", ect)
        baseURL:Config.moviesURL, // Base URL (localhost:8081 for example)
        headers: {
            Authorization: "Bearer " + accessToken
        },
        url: Config.movies.id + id, // Path of URL ("/login")
    }

    return Axios.request(options);
}

export async function insertCart(cartRequest, accessToken) {
    const requestBody = {
        movieId: cartRequest.movieId,
        quantity: cartRequest.quantity
    };

    const options = {
        method: "POST", // Method type ("POST", "GET", "DELETE", ect)
        baseURL: Config.cartURL, // Base URL (localhost:8081 for example)
        headers: {
            Authorization: "Bearer " + accessToken
        },
        url: Config.carts.insert, // Path of URL ("/login")
        data: requestBody // Data to send in Body (The RequestBody to send)
    }

    return Axios.request(options);
}


export async function retrieveCart(accessToken) {
    const options = {
        method: "GET", // Method type ("POST", "GET", "DELETE", ect)
        baseURL:Config.cartURL, // Base URL (localhost:8081 for example)
        headers: {
            Authorization: "Bearer " + accessToken
        },
        url: Config.carts.retrieve, // Path of URL ("/login")
    }

    return Axios.request(options);
}

export async function updateCart(cartRequest, accessToken) {

    const requestBody = {
        movieId: cartRequest.movieId,
        quantity: cartRequest.quantity
    };

    const options = {
        method: "POST", // Method type ("POST", "GET", "DELETE", ect)
        baseURL: Config.cartURL, // Base URL (localhost:8081 for example)
        headers: {
            Authorization: "Bearer " + accessToken
        },
        url: Config.carts.update, // Path of URL ("/login")
        data: requestBody // Data to send in Body (The RequestBody to send)
    }

    return Axios.request(options);
}

export async function clearCart(accessToken) {

    const options = {
        method: "POST", // Method type ("POST", "GET", "DELETE", ect)
        baseURL: Config.cartURL, // Base URL (localhost:8081 for example)
        headers: {
            Authorization: "Bearer " + accessToken
        },
        url: Config.carts.clear, // Path of URL ("/login")
    }
    
    return Axios.request(options);
}

export async function deleteCart(movieId, accessToken) {

    const options = {
        method: "DELETE", // Method type ("POST", "GET", "DELETE", ect)
        baseURL: Config.cartURL, // Base URL (localhost:8081 for example)
        headers: {
            Authorization: "Bearer " + accessToken
        },
        url: Config.carts.delete + movieId, // Path of URL ("/login")
    }
    
    return Axios.request(options);
}

export async function listOrder(accessToken) {

    const options = {
        method: "GET", // Method type ("POST", "GET", "DELETE", ect)
        baseURL: Config.cartURL, // Base URL (localhost:8081 for example)
        headers: {
            Authorization: "Bearer " + accessToken
        },
        url: Config.orders.list,  // Path of URL ("/login")
    }
    
    return Axios.request(options);
}



export default {post,posts, insertCart, updateCart, 
                retrieveCart, clearCart, deleteCart, listOrder}


