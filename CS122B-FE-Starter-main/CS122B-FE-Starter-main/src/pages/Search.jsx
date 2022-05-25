import React, { useEffect } from "react";
import {useUser} from "hook/User";
import styled from "styled-components";
import {useForm} from "react-hook-form";
import {search} from "backend/idm";
import 'bootstrap/dist/css/bootstrap.min.css';
import InputGroup from 'react-bootstrap/InputGroup';
import {FormControl, FormGroup, ControlLabel} from 'react-bootstrap';
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'
import Table from 'react-bootstrap/Table'
import {useNavigate} from "react-router-dom"

const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
`


const Search = () => {

    const [movies, setMovies] = React.useState([]);
    const {accessToken} = useUser();
    const [page, setPage] = React.useState(1);
    const {register, getValues, handleSubmit} = useForm();
    const navigate = useNavigate();


    useEffect(() => {}, [page])

    const submitSearch = (myPage) => {
        const input = getValues("input");
        const searchBy = getValues("searchBy");
        const sortBy = getValues("sortBy");
        const orderBy = getValues("orderBy");
        const limit = getValues("limit");

        let title = null;
        let year = null;
        let director = null;
        let genre = null;

        title = getValues("title");
        year = getValues("year");
        director = getValues("director");
        genre = getValues("genre");

        
        // if(searchBy === "title")
        // {
        //     title = input;
        // }
        // if(searchBy === "year")
        // {
        //     year = input;
        // }
        // if(searchBy === "genre")
        // {
        //     genre = input;
        // }
        // if(searchBy === "director")
        // {
        //     director = input;
        // }

        const payLoad = {
            title : title, 
            year : year, 
            director : director,  
            genre : genre, 
            limit : limit,
            page : myPage,
            orderBy : sortBy,
            direction : orderBy,
            accessToken : accessToken
        }

        console.log(payLoad)

        search(payLoad)
            .then((response) => setMovies(response.data.movies))
            .catch(error => console.log(error.response.data));

        console.log(movies);
        console.log(page)
    }

    return (
        <div>
            <div>
                <h1>Search</h1>
                <InputGroup>
                    <InputGroup.Text>Title</InputGroup.Text>
                    <FormControl
                    placeholder="Enter Title"
                    aria-label="Title"
                    {...register("title")}
                    />
                </InputGroup>

                <InputGroup>
                    <InputGroup.Text>Year</InputGroup.Text>
                    <FormControl
                    placeholder="Enter Year"
                    aria-label="Year"
                    {...register("year")}
                    />
                </InputGroup>

                <InputGroup>
                    <InputGroup.Text>Director</InputGroup.Text>
                    <FormControl
                    placeholder="Enter Director"
                    aria-label="Director"
                    {...register("director")}
                    />
                </InputGroup>

                <InputGroup>
                    <InputGroup.Text>Genre</InputGroup.Text>
                    <FormControl
                    placeholder="Enter Genre"
                    aria-label="Genre"
                    {...register("genre")}
                    />
                </InputGroup>            

{/*                 
                <h3>Filter</h3>
                <select {...register("searchBy")}>
                    <option value="title">title</option>
                    <option value="year">year</option>
                    <option value="director">director</option>
                    <option value="genre">genre</option>
                </select> */}

            </div>
            
            <br/>

            <div>
                <h1>Pagination</h1>
                    
                    <Form.Select {...register("sortBy")}>
                        <option value="title">Sort By </option>
                        <option value="title">title</option> 
                        <option value="rating">rating</option>
                        <option value="year">year</option>
                    </Form.Select>


                    <Form.Select {...register("orderBy")}>
                        <option value="asc">Order By </option>
                        <option value="asc">asc</option>
                        <option value="desc">desc</option>
                    </Form.Select>

                    <Form.Select {...register("limit")}>
                        <option value="10"> Limit </option>
                        <option value="10">10</option>
                        <option value="25">25</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </Form.Select>



                <br/>
                <br/>
                <Button variant="secondary" onClick={(handleSubmit) => {submitSearch(page)}}>Submit</Button>
            </div>


            <br/>


            <div className = "Table">
                <h1> Result Area </h1>

                <Table variant="dark">
                    <tbody>
                        <tr>
                            <th>Title</th>
                            <th>Year</th>
                            <th>Director</th>
                            <th>Detail</th>
                        </tr>
                        
                        {movies?.map((movie, key) => 
                            {
                            return (
                                <tr key={key}>
                                    <td>{movie.title}</td>
                                    <td>{movie.year}</td>
                                    <td>{movie.director}</td>
                                    <td>

                                        <Button onClick={() => {navigate("/movie/" + movie.id)}}>Details</Button>

                                    </td>
                                </tr>
                            )})
                        }
                    </tbody>
                </Table>
            </div>

            <br/>

            {/* https://stackoverflow.com/questions/59304283/error-too-many-re-renders-react-limits-the-number-of-renders-to-prevent-an-in */}
            <h5>Current page :  [{page}] </h5>
            <Button variant="secondary" onClick={() => { 
                                        if(page - 1 < 1) 
                                        {
                                            setPage(1);
                                        }
                                        else
                                        {
                                            setPage(page - 1);
                                            submitSearch(page - 1);
                                        } 
                                    }
                            }>Prev Page </Button>

            <Button variant="secondary" onClick={() => { 
                                        setPage(page + 1); 
                                        submitSearch(page + 1);
                                    }
                            }>Next Page </Button>

        </div>
    )
}

export default Search;
