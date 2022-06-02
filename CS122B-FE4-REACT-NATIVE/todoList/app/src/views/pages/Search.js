import 'react-native-gesture-handler';
import React, {useState, useEffect} from 'react';
import COLORS from '../../consts/color';
import STYLES from '../../styles';
import {ScrollView, TouchableOpacity} from 'react-native-gesture-handler';
import {search} from '../../backend/idm';
import AsyncStorage from "@react-native-async-storage/async-storage";
import RNPickerSelect from 'react-native-picker-select';
import { FlatList, SafeAreaView, View, StatusBar, StyleSheet, Text, TextInput } from "react-native";

const Item = ({ item, onPress, backgroundColor, textColor }) => (
    <TouchableOpacity 
        onPress={onPress}
        style={[styles.item, backgroundColor]}>
      <Text style={[styles.title, styles.year, styles.director, textColor]}> : {item.title} -- {item.year} -- By : {item.director}</Text>
    </TouchableOpacity>
  );

function Search({navigation}) {
    const [selectedId, setSelectedId] = useState(null);
    const [movies, setMovies] = React.useState([]);


    const [sortBy, setSortBy] = useState("title");
    const [orderBy, setOrderBy] = useState("asc");
    const [limit, setLimit] = useState("10");
    const [page, setPage] = React.useState(1);

    const [title, setTitle] = useState("");
    const [year, setYear] = useState("");
    const [director, setDirector] = useState("");
    const [genre, setGenre] = useState("");

    useEffect(() => {}, [page])


    const submitSearch = async (myPage) => {

        const payLoad = {
            title : title, 
            year : year, 
            director : director,  
            genre : genre, 
            limit : limit,
            page : myPage,
            orderBy : sortBy,
            direction : orderBy,
            accessToken : await AsyncStorage.getItem("accessToken")
        }

        console.log(payLoad)

        search(payLoad)
            .then((response) => setMovies(response.data.movies))
            .catch(error => console.log(error.response.data));

        console.log(movies);
        console.log(page);
    };

    const renderItemMovie = ({ item }) =>
    {
        const backgroundColor = item.id === selectedId ? COLORS.darkPink : COLORS.lightPink;
        const color = item.id === selectedId ? 'white' : 'black';

        return (
            <Item
              item={item}
              onPress={() => {
                setSelectedId(item.id);
                navigation.navigate('MovieDetail', {id: item.id});
              }}
              backgroundColor={{ backgroundColor }}
              textColor={{ color }}
            />
          );
    };


    return (
        <SafeAreaView style={{paddingHorizontal: 20, flex: 1, backgroundColor: COLORS.brown}}>
            <ScrollView showsVerticalScrollIndicator={false}>
                <View style={{flexDirection: 'row', marginTop: 40}}>
                    <Text style={{fontWeight: 'bold', fontSize: 22, color: COLORS.beige}}>
                        punsalaa
                    </Text>
                    <Text
                        style={{fontWeight: 'bold', fontSize: 22, color: COLORS.orange}}>
                        MOVIE_SERVICE
                    </Text>
                </View>
    
                <View style={{marginTop: 20}}>
                    <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                        Search For A Movie
                    </Text>
                </View>


                <View style={{marginTop: 20}}>
                    <Text style={{fontSize: 20, fontWeight: 'bold', color: COLORS.beige}}>
                        Input Details
                    </Text>
                </View>

                <View style={{marginTop: 5}}>
                    <View style={STYLES.inputContainer}>
                        <TextInput 
                            placeholder="Title" 
                            style={STYLES.input} 
                            value={title}
                            onChangeText={setTitle}
                            />
                    </View>

                    <View style={STYLES.inputContainer} >
                        <TextInput
                        placeholder="Year"
                        style={STYLES.input}
                        value={year}
                        onChangeText={setYear}
                        />
                    </View>


                    <View style={STYLES.inputContainer} >
                        <TextInput
                        placeholder="Director"
                        style={STYLES.input}
                        value={director}
                        onChangeText={setDirector}
                        />
                    </View>
 
                    <View style={STYLES.inputContainer} >
                        <TextInput
                        placeholder="Genre"
                        style={STYLES.input}
                        value={genre}
                        onChangeText={setGenre}
                        />
                    </View>

                    <View style={{marginTop: 20}}>
                        <Text style={{fontSize: 20, fontWeight: 'bold', color: COLORS.beige}}>
                            Filters 
                        </Text>
                    </View>

                    <Text style={{textAlign: "center", fontSize: 15, fontWeight: 'bold', color: COLORS.beige}}>
                        Sort By 
                    </Text>
                
                    <RNPickerSelect
                        placeholder={{
                                label: 'Title',
                                value: 'title'}}
                        onValueChange={value => setSortBy(value)}
                        items={[
                            { label: 'Rating', value: 'rating' },
                            { label: 'Year', value: 'year' },
                        ]}
                    />

                    <Text style={{textAlign: "center", fontSize: 15, fontWeight: 'bold', color: COLORS.beige}}>
                        Order By 
                    </Text>
                    
                    <RNPickerSelect
                        placeholder={{
                                label: 'Ascending',
                                value: 'asc'}}
                        onValueChange={value => setOrderBy(value)}
                        items={[
                            { label: 'Descending', value: 'desc' },
                        ]}
                    />

                    <Text style={{textAlign: "center", fontSize: 15, fontWeight: 'bold', color: COLORS.beige}}>
                        Limit 
                    </Text>
                        
                    <RNPickerSelect
                        placeholder={{
                                label: '10',
                                value: "10"}}
                        onValueChange={value => setLimit(value)}
                        items={[
                            { label: '25', value: "25"},
                            { label: '50', value: "50" },
                            { label: '100', value: "100" },
                        ]}
                    />

                    <View style={STYLES.btnPrimary}>
                        <TouchableOpacity onPress={() => {submitSearch(page);}}>
                            <Text style={{color: '#fff', fontWeight: 'bold', fontSize: 18}}>
                                Search 
                            </Text>
                        </TouchableOpacity>
                    </View>


                    <FlatList
                        data={movies}
                        renderItem={renderItemMovie}
                        keyExtractor={(item) => item.id}
                        extraData={selectedId}
                    />

                    <View style={STYLES.secondaryPrimary}>
                        <TouchableOpacity onPress={() => {

                            setPage(page + 1);
                            submitSearch(page + 1);
                        
                        }}>
                            <Text style={{color: '#fff', fontWeight: 'bold', fontSize: 18}}>
                                NextPage 
                            </Text>
                        </TouchableOpacity>
                    </View>

                    <View style={STYLES.secondaryPrimary}>
                        <TouchableOpacity onPress={() => {

                            if(page - 1 < 1) 
                            {
                                setPage(1);
                            }
                            else
                            {
                                setPage(page - 1);
                                submitSearch(page - 1);
                            } 
                        }}>
                            <Text style={{color: '#fff', fontWeight: 'bold', fontSize: 18}}>
                                Previous Page 
                            </Text>
                        </TouchableOpacity>
                    </View>
    
                    <View style={{marginTop: 20}}>
                        <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                            Current Page : [{page}]; 
                        </Text>
                    </View>




                </View>
            </ScrollView>
        </SafeAreaView>
      );
    };

export default Search;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: StatusBar.currentHeight || 0,
  },
  item: {
    padding: 20,
    marginVertical: 8,
    marginHorizontal: 16,
  },
  title: {
    fontSize: 25,
  },
  year: {
    fontSize: 25,
  },
  director: {
    fontSize: 25,
  },



});