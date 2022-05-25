import React, { useState } from "react";
import styled from "styled-components";
import {useForm} from "react-hook-form";
import {reg} from "backend/idm";
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

	const { accessToken } = useUser();
	const navigate = useNavigate();

    const [items, setItems] = useState([]);
    const [total, setTotal] = useState(0);
    const [result, setResult] = useState();
    const [quantity, setQuantity] = useState();

    React.useEffect(()=>{
        JSONPlaceHolder
        .retrieveCart(accessToken)
        .then(response => {setTotal(response.data.total);
                           setResult(response.data.result);
                           setItems(response.data.items);
                          })},[])

    const cartUpdate = (id, quantity) => {

        const payLoad = {
            quantity : quantity,
            movieId : id
        }

        console.log(payLoad)
            JSONPlaceHolder
            .updateCart(payLoad, accessToken)
            .then(response => setResult(response.data.result))

        JSONPlaceHolder
        .retrieveCart(accessToken)
        .then(response => {setTotal(response.data.total);
                           setResult(response.data.result);
                           setItems(response.data.items);})
    }





    return (
        <div className = "Table">
                <h1>  Cart </h1>

                <Table variant="dark">
                    <tbody>
                        <tr>
                            <th>Title</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Update</th>
                        </tr>
                        
                        {items?.map((item, key) => 
                            {
                            return (

                                <tr 
                                    key={key}>
                                    <td>{item.movieTitle}</td>
                                    <td>{item.unitPrice}</td>
                                    <td>{item.quantity} </td>
                                    <td>
                                        <div>
                                            <Button variant="secondary" onClick={() => {                                        
                                                                    if(quantity - 1 < 1) 
                                                                    {
                                                                        cartUpdate(item.movieId, 1);
                                                                    }
                                                                    else
                                                                    {
                                                                        cartUpdate(item.movieId, (quantity - 1));
                                                                    } 
                                                                }
                                                            }>-</Button>

                                            <Button variant="secondary" onClick={() => { 
                                                                    cartUpdate(item.movieId, (quantity + 1)); 
                                                                }
                                                        }>+</Button>

                                        </div> 
                                    </td>
                                </tr>
                            )})
                        }
                    </tbody>
                </Table>

                <h1>  Total : ${total}</h1>

        </div>
    );
}


export default MovieDetail;