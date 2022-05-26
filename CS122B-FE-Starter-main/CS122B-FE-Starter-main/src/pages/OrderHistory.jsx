import React, { useEffect, useState } from "react";
import styled from "styled-components";
import {useNavigate, useParams } from "react-router-dom";
import JSONPlaceHolder from "backend/JSONPlaceHolder";
import { useUser } from "hook/User";
import Table from 'react-bootstrap/Table'
import Button from 'react-bootstrap/Button'

const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
`

const StyledH1 = styled.h1`
  display: flex;
  flex-direction: column;
`

const StyledInput = styled.input`
`

const StyledButton = styled.button`
`

const MovieDetail = () => {

	const {accessToken } = useUser();
    const [items, setItems] = useState([]);
    const [total, setTotal] = useState(0);
    const [result, setResult] = useState();
    const {movieId} = useParams();
    const navigate = useNavigate();

    useEffect(()=>{
        JSONPlaceHolder
        .retrieveCart(accessToken)
        .then(response => {setTotal(response.data.total);
                           setResult(response.data.result);
                           setItems(response.data.items);
                          }
             )
    },[])

  
    async function cartDelete(id)
    {
        await JSONPlaceHolder
        .deleteCart(id, accessToken)
        .then(response => setResult(response.data.result))
    }    

    async function cartUpdate (id, q) {

        const payLoad =  {
            movieId : id,
            quantity : q
        }

        console.log(payLoad)
            await JSONPlaceHolder
            .updateCart(payLoad, accessToken)
            .then(response => setResult(response.data.result))
    }

    async function cartClear(){
            await JSONPlaceHolder
            .clearCart(accessToken)
            .then(response => setResult(response.data.result))
    }

    return ()
}

export default OrderHistory;