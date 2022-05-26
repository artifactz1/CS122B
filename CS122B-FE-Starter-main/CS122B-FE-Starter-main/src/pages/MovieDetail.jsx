import React from "react";
import styled from "styled-components";
import {useForm} from "react-hook-form";
import {reg} from "backend/idm";
import {useNavigate, useParams } from "react-router-dom";
import JSONPlaceHolder from "backend/JSONPlaceHolder";
import { useUser } from "hook/User";
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

    const {accessToken} = useUser();
    const {id} = useParams();
    const [p, setP] = React.useState();
    const [quantity, setQuantity] = React.useState(1);
    const [result, setResult] = React.useState();

    React.useEffect(()=>{
        JSONPlaceHolder
        .post(id, accessToken)
        .then(response => setP(response.data))
    },[])

    const submitCart = (id, quantity) => {

        const payLoad = {
            quantity : quantity,
            movieId : id
        }

        console.log(payLoad)
            JSONPlaceHolder
            .insertCart(payLoad, accessToken)
            .then(response => setResult(response.data.result))
    }



    return (
        <StyledDiv> {p && 
            <React.Fragment>
                <h1>Title : {p.movie.title}</h1>
                <img src = {"https://image.tmdb.org/t/p/w500/" + p.movie.posterPath}
                     alt = "new" 
                />
{/* 
                <h2>Genres : 
                    {p.movie.map(n => (
                                                <div>
                                                    {n.genres.name}
                                                </div>
                                                ))}
                </h2> */}
                
                <h2>Overview : {p.movie.overview}</h2>
                <h2>Director : {p.movie.director}</h2>
                <h2>Rating : {p.movie.rating}</h2>
                <h2>Year : {p.movie.year}</h2>
                <h2>Number of Votes : {p.movie.numVotes}</h2>
                <h2>Budget : {p.movie.budget}</h2>
                <h2>Revenue : {p.movie.revenue}</h2>

                <div>
                    <h5>Current Quantity :  [{quantity}] </h5>
                    <Button variant="secondary" onClick={() => {                                        
                                            if(quantity - 1 < 1) 
                                            {
                                                setQuantity(1);
                                            }
                                            else
                                            {
                                                setQuantity(quantity - 1);
                                            } 
                                        }
                                    }>-</Button>
                    <Button variant="secondary" onClick={() => { 
                                            setQuantity(quantity + 1); 
                                        }
                                }>+</Button>

                    <Button variant="secondary" onClick={() => { 
                                        submitCart(id, quantity);
                                    }
                            }> Add to Cart </Button>


                </div> 
                <div>
                    {/* <p>Message : {result.message}</p> */}
                </div>

            </React.Fragment>

        }
        </StyledDiv>
        // <div>
        //     <h1>DETAIL</h1>
        //     <p>{id}</p>
        //     <p>{p.id}</p>
        
        // </div>

    );
}


export default MovieDetail;