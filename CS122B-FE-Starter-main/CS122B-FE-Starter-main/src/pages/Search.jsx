import React from "react";
import {useUser} from "hook/User";
import styled from "styled-components";
import {useForm} from "react-hook-form";
import {search} from "backend/idm";
import 'bootstrap/dist/css/bootstrap.min.css';
import InputGroup from 'react-bootstrap/InputGroup';
import {FormControl, FormGroup, ControlLabel} from 'react-bootstrap';
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'


const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
`


const Search = () => {

    const [movies, setMovies] = React.useState([]);
    const {accessToken} = useUser();
    const [page, setPage] = React.useState(1);
    const {register, getValues, handleSubmit} = useForm();

    const submitSearch = () => {
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
            page : page,
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

                <InputGroup className="mb-3">
                    <InputGroup.Text>Title</InputGroup.Text>
                    <FormControl
                    placeholder="Enter Title"
                    aria-label="Title"
                    {...register("year")}
                    />
                </InputGroup>

               

                 <InputGroup className="mb-3">
                    <InputGroup.Text>Year</InputGroup.Text>
                    <FormControl
                    placeholder="Enter Year"
                    aria-label="Year"
                    {...register("year")}
                    />
                </InputGroup>

                <InputGroup className="mb-3">
                    <InputGroup.Text>Director</InputGroup.Text>
                    <FormControl
                    placeholder="Enter Director"
                    aria-label="Director"
                    {...register("director")}
                    />
                </InputGroup>

                <InputGroup className="mb-3">
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
                        <option value="asc">title</option>
                        <option value="desc">year</option>
                    </Form.Select>

                    <Form.Select {...register("limit")}>
                        <option value="10"> Limit </option>
                        <option value="10">10</option>
                        <option value="25">25</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </Form.Select>

                <br/>

                {/* https://stackoverflow.com/questions/59304283/error-too-many-re-renders-react-limits-the-number-of-renders-to-prevent-an-in */}
                <h5>Current page :  [{page}] </h5>
                <Button variant="secondary" onClick={() => { 
                                        setPage(page - 1);
                                        if(page < 2) 
                                        {
                                            setPage(1);
                                        } 
                                        handleSubmit(submitSearch)
                                       }
                                }>Prev Page </Button>

                <Button variant="secondary" onClick={() => { 
                                        setPage(page + 1); 
                                        handleSubmit(submitSearch)
                                       }
                                }>Next Page </Button>


                <br/>
                <br/>
                <Button variant="secondary" onClick={handleSubmit(submitSearch)}>Submit</Button>
            </div>


            <br/>


            <div className = "Table">
                <h1> Result Area </h1>
                
                <table>
                    <tbody>
                        <tr>
                            <th>id</th>
                            <th>title</th>
                            <th>year</th>
                            <th>director</th>
                            <th>rating</th>
                        </tr>
                        
                        {movies?.map((movie, key) => 
                            {
                            return (
                                <tr key={key}>
                                    <td>{movie.id}</td>
                                    <td>{movie.title}</td>
                                    <td>{movie.year}</td>
                                    <td>{movie.director}</td>
                                    <td>{movie.rating}</td>
                                </tr>
                            )
                            }
                            )
                        }
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default Search;
