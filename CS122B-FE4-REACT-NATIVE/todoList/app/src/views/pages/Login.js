import 'react-native-gesture-handler';
import React, {useState} from 'react';
import {SafeAreaView, View, Text, TextInput} from 'react-native';
import COLORS from '../../consts/color';
import STYLES from '../../styles';
import {ScrollView, TouchableOpacity} from 'react-native-gesture-handler';
import {loginSub} from '../../backend/idm';
import AsyncStorage from "@react-native-async-storage/async-storage";

function Login({navigation}) {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    
    const submitLogin = async () => {

        const payLoad = {
            email: email,
            password: password.split('')
        }

        return await loginSub(payLoad)
            // .then(response => alert(JSON.stringify(response.data, null, 2)))

            .then((response) => {
                // setAccessToken(response.data.accessToken);
                // setRefreshToken(response.data.refreshToken);
                alert(JSON.stringify(response.data, null, 2)); 
                console.log(response);
                console.log(response.data.accessToken);
                AsyncStorage.setItem("accessToken", response.data.accessToken);
                AsyncStorage.setItem("refreshToken", response.data.refreshToken);
                return response;
            })
            .catch(error => alert(JSON.stringify(error.response.data, null, 2)))
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
                  Login
              </Text>
            </View>

            <View style={{marginTop: 5}}>

              <View style={STYLES.inputContainer}>
                <TextInput 
                    placeholder="Email" 
                    style={STYLES.input} 
                    value={email}
                    onChangeText={setEmail}
                    />
              </View>

              <View style={STYLES.inputContainer} >
                <TextInput
                  placeholder="Password"
                  style={STYLES.input}
                  value={password}
                  onChangeText={setPassword}
                  secureTextEntry
                />
              </View>
                           

              <View style={STYLES.btnPrimary}>
                <TouchableOpacity onPress={async () => {
                        const response = await submitLogin(); 
                        console.log(response.data.result.code);                       

                        if(response === undefined){
                            navigation.navigate('Login');
                        }
                        else {
                            if(response.data.result.code === 1020)
                            {
                                navigation.navigate('Search');
                            }
                        }
                    }}>
                    <Text style={{color: '#fff', fontWeight: 'bold', fontSize: 18}}>
                        Sign In
                    </Text>
                </TouchableOpacity>
              </View>
              
              <View
                style={{
                  marginVertical: 20,
                  flexDirection: 'row',
                  justifyContent: 'center',
                  alignItems: 'center',
                }}>
              </View>

            </View>
    
            <View
              style={{
                flexDirection: 'row',
                alignItems: 'flex-end',
                justifyContent: 'center',
                marginTop: 40,
                marginBottom: 20,
              }}>
              <Text style={{color: COLORS.light, fontWeight: 'bold'}}>
                If you don`t have an account ->       
              </Text>
              <TouchableOpacity onPress={() => navigation.navigate('Register')}>
                <Text style={{color: COLORS.pink, fontWeight: 'bold'}}>
                    Register 
                </Text>
              </TouchableOpacity>
            </View>
          </ScrollView>
        </SafeAreaView>
      );
    };

export default Login;