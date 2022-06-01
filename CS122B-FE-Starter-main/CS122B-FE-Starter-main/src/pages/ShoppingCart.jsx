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

const ShoppingCart = () => {

	const {accessToken } = useUser();
    const [items, setItems] = useState([]);
    const [total, setTotal] = useState(0);
    const [result, setResult] = useState();
    const {movieId} = useParams();
    const navigate = useNavigate();

    async function updateTable()
    {
        JSONPlaceHolder
        .retrieveCart(accessToken)
        .then(response => {setTotal(response.data.total);
                           setResult(response.data.result);
                           setItems(response.data.items);
                          }
             )
    }
   
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

        updateTable();
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

            updateTable();
         }

    async function cartClear(){
            await JSONPlaceHolder
            .clearCart(accessToken)
            .then(response => setResult(response.data.result))

            updateTable();
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
                                                                    if(item.quantity - 1 >= 1) 
                                                                    {
                                                                        cartUpdate(item.movieId, item.quantity - 1);
                                                                    } 
                                                                }
                                                            }>-</Button>

                                            <Button variant="secondary" onClick={() => { 
                                                                        cartUpdate(item.movieId, item.quantity + 1); 
                                                                    }
                                                        }>+</Button>

                                            <Button variant="secondary" onClick={() => { 
                                                                        cartDelete(item.movieId); 
                                                                    }
                                                        }>DELETE</Button>
                                        </div> 
                                    </td>
                                </tr>
                            )})
                        }
                    </tbody>
                </Table>
                
                
                <h1>  Total : ${total}</h1>
                <div>
                            <Button variant="secondary" onClick={() => { 
                                                    cartClear(); 
                                                }
                                        }>CLEAR CART</Button>
                            <Button variant="secondary" onClick={() => { 
                                                navigate("/order");     
                                            }
                                        }> CHECKOUT </Button>
                </div>
        </div>
    );
}

export default ShoppingCart;