import 'react-native-gesture-handler';
import React, {useState} from 'react';
import {SafeAreaView, View, Text, TextInput} from 'react-native';
import COLORS from '../../consts/color';
import STYLES from '../../styles';
import {ScrollView, TouchableOpacity} from 'react-native-gesture-handler';
import {reg} from '../../backend/idm';

function Register({navigation}) {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const submitReg = async () => {

        const payLoad = {
            email: email,
            password: password.split('')
        }

        return await reg(payLoad)
            .then(response => {
                alert(JSON.stringify(response.data, null, 2)); 
                console.log(response);
                //console.log(response.data);
                console.log(response.data.result.code);
                return response.data.result.code;
            })
            .catch(error => alert(JSON.stringify(error.response.data, null, 2)))
    }

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
                  Register
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
                        const res = await submitReg();
                        console.log("RESSSSPONSE RESULT");
                        console.log(res);
                        if(res === 1010)
                        {
                            navigation.navigate('Login');
                        }
                    }}>
                    <Text style={{color: '#fff', fontWeight: 'bold', fontSize: 18}}>
                        Create Account 
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
                marginBottom: 20,
              }}>
              <Text style={{color: COLORS.light, fontWeight: 'bold'}}>
                If you have an account already 
              </Text>
              <TouchableOpacity onPress={() => navigation.navigate('Login')}>
                <Text style={{color: COLORS.pink, fontWeight: 'bold'}}>
                    Login 
                </Text>
              </TouchableOpacity>
            </View>
          </ScrollView>
        </SafeAreaView>
      );
    };

export default Register;