import 'react-native-gesture-handler';
import React, {useState} from 'react';
import {SafeAreaView, View, Text, TextInput} from 'react-native';
import COLORS from '../../consts/color';
import STYLES from '../../styles';
import {ScrollView, TouchableOpacity} from 'react-native-gesture-handler';
import {loginSub} from '../../backend/idm';
import AsyncStorage from "@react-native-async-storage/async-storage";
import RNPickerSelect from 'react-native-picker-select';

function Search({navigation}) {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [sortBy, setSortBy] = useState("title");
    const [orderBy, setOrderBy] = useState("asc");
    const [limit, setLimit] = useState("10");
    const [page, setPage] = React.useState(1);

    const [title, setTitle] = useState("");
    const [year, setYear] = useState("");
    const [director, setDirector] = useState("");
    const [genre, setGenre] = useState("");

    const submitSearch = () => {

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
        <SafeAreaView
          style={{paddingHorizontal: 20, flex: 1, backgroundColor: COLORS.brown}}>
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
                <TouchableOpacity onPress={() => {submitSearch();}}>
                    <Text style={{color: '#fff', fontWeight: 'bold', fontSize: 18}}>
                        Search 
                    </Text>
                </TouchableOpacity>
              </View>
            </View>
          </ScrollView>
        </SafeAreaView>
      );
    };

export default Search;

