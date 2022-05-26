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
    const {movieId} = useParams();

    React.useEffect(async ()=>{
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
        JSONPlaceHolder
        .deleteCart(id, accessToken)
        .then(response => setResult(response.data.result))
    }    

    async function cartUpdate (id, q) {

        const payLoad =  {
            movieId : id,
            quantity : q
        }

        console.log(payLoad)
            JSONPlaceHolder
            .updateCart(payLoad, accessToken)
            .then(response => setResult(response.data.result))

    }

    async function cartClear(){

            JSONPlaceHolder
            .clearCart(accessToken)
            .then(response => setResult(response.data.result))
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
                                            <Button variant="secondary" onClick={() => { 
                                                                    cartClear(); 
                                                                }
                                                        }>CLEAR CART</Button>


                <h1>  Total : ${total}</h1>
                {/* <h1>  Mesesage : {result.message}</h1> */}

        </div>
    );
}

export default MovieDetail;