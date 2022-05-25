import React from "react";
import styled from "styled-components";
import {useForm} from "react-hook-form";
import {reg} from "backend/idm";
import {useNavigate, useParams } from "react-router-dom";
import JSONPlaceHolder from "backend/JSONPlaceHolder";
import { useUser } from "hook/User";

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

    return (
        <h1></h1>
    );
}


export default MovieDetail;