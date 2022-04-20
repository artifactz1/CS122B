import React from "react";
import styled from "styled-components";
import {useForm} from "react-hook-form";
import {reg} from "backend/idm";


const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
`


const Reg = () => {

    const {register, getValues, handleSubmit} = useForm();

    const submitReg = () => {
        const email = getValues("email");
        const password = getValues("password");

        const payLoad = {
            email: email,
            password: password.split('')
        }

        reg(payLoad)
            .then(response => alert(JSON.stringify(response.data, null, 2)))
            .catch(error => alert(JSON.stringify(error.response.data, null, 2)))
    }

    return (
        <StyledDiv>
            <h1>Register</h1>
            <input {...register("email")} type={"email"}/>
            <input {...register("password")} type={"password"}/>
            <button onClick={handleSubmit(submitReg)}>Register</button>
        </StyledDiv>
    );
}

export default Reg;
