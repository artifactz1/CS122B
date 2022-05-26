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

const OrderHistory = () => {

    const {accessToken} = useUser();
    const [sales, setSales] = useState([5]);
    const [result, setResult] = useState();
   
    useEffect(() => {
            JSONPlaceHolder
        .listOrder(accessToken)
        .then(response => {setSales(response.data.sales)
                           setResult(response.data.result)
                          } 
             )

    }, [])

    return (
            <div className = "Table">
                <h1> PREVIOUS ORDERS </h1>
                <Table variant="dark">
                    <tbody>
                        <tr>
                            <th>Sale ID</th>
                            <th>Total</th>
                            <th>Order Date</th>
                        </tr>
                        
                        {sales?.map((sale, key) => 
                            {
                            return (
                                <tr key={key}>
                                    <td>{sale.saleId}</td>
                                    <td>{sale.total}</td>
                                    <td>{sale.orderDate}</td>
                                </tr>
                            )})
                        }
                    </tbody>
                </Table>
            </div>
    );
}

export default OrderHistory;