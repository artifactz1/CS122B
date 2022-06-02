import 'react-native-gesture-handler';
import React, {useState, useEffect} from 'react';
import COLORS from '../../consts/color';
import STYLES from '../../styles';
import {ScrollView, TouchableOpacity} from 'react-native-gesture-handler';
import {search} from '../../backend/idm';
import JSONPlaceHolder from '../../backend/JSONPlaceHolder';
import AsyncStorage from "@react-native-async-storage/async-storage";
import RNPickerSelect from 'react-native-picker-select';
import { FlatList, SafeAreaView, View, StatusBar, StyleSheet, Text, TextInput, Image } from "react-native";
import { RouteProp, useRoute } from "@react-navigation/native";


function MovieDetail({route, navigation})
{
    const { id } = route.params; 
    const [p, setP] = React.useState();

    const getDetail = async () => {
        const accessToken = await AsyncStorage.getItem("accessToken");

            JSONPlaceHolder
            .post(id, accessToken)
            .then(response =>{
                console.log(response);
                setP(response.data.movie);
                return response.data.movie;
            });
    };

    useEffect(() =>{
        const getData = async () => {
            const res = await getDetail();
            setP(res);
        };
        getData();
    },[]);

    return (
        <SafeAreaView
          style={{paddingHorizontal: 20, flex: 1, backgroundColor: COLORS.brown}}>
          <ScrollView showsVerticalScrollIndicator={false}>
            <View style={{flexDirection: 'row', marginTop: 40}}>
              <Text 
                style={{fontWeight: 'bold', fontSize: 22, color: COLORS.beige}}>
                punsalaa
              </Text>
              <Text
                style={{fontWeight: 'bold', fontSize: 22, color: COLORS.orange}}>
                MOVIE_SERVICE
              </Text>
            </View>
    
            <View style={{marginTop: 20}}>
              <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                  Movie Detail
              </Text>
            </View>

            
            <View style={{marginTop: 20}}>
                <Image
					source={{
						uri: "https://image.tmdb.org/t/p/w300" + p?.posterPath,
                        justifyContent: 'center',
                        alignItems: 'center',
					}}
					style={{width: 250, height:250}}
				></Image>
				<Image
					source={{
						uri: "https://image.tmbd.org/t/p/w300" + p?.backdropPath,
                        justifyContent: 'center',
                        alignItems: 'center',
					}}
					style={{width: 250, height:250}}

				></Image>
 
                 <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                Title : {p?.title}
                </Text>

               
                <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                Overview : {p?.overview}
                </Text>

                 
                <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                Director : {p?.director}
                </Text>
                <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                Rating : {p?.rating}
                </Text>
                <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                Year : {p?.year}
                </Text>   
                <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                Number of Votes : {p?.numVotes}
                </Text>
                <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                Budget : {p?.budget}
                </Text>
                <Text style={{textAlign: "center", fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                Revenue : {p?.revenue}
                </Text>
            </View>

            <View style={STYLES.btnPrimary}>
                <TouchableOpacity onPress={async () => {
                            navigation.navigate('Search');
                    }}>
                    <Text style={{color: '#fff', fontWeight: 'bold', fontSize: 18}}>
                        Go Back to Search  
                    </Text>
                </TouchableOpacity>
            </View>


          </ScrollView>
        </SafeAreaView>
      );
};

export default MovieDetail;