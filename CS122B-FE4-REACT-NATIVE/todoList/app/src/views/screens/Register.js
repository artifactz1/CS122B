import 'react-native-gesture-handler';
import React from 'react';
import {SafeAreaView, View, Text, TextInput} from 'react-native';
import COLORS from '../../consts/color';
import STYLES from '../../styles';
import {ScrollView, TouchableOpacity} from 'react-native-gesture-handler';

function Register({navigation}) {
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
              <Text style={{fontSize: 27, fontWeight: 'bold', color: COLORS.beige}}>
                  Register
              </Text>
            </View>
            <View style={{marginTop: 5}}>
              <View style={STYLES.inputContainer}>
                <TextInput placeholder="Email" style={STYLES.input} />
              </View>
              <View style={STYLES.inputContainer}>
                <TextInput
                  placeholder="Password"
                  style={STYLES.input}
                  secureTextEntry
                />
              </View>
              
              <View style={STYLES.btnPrimary}>
                <TouchableOpacity onPress={() => navigation.navigate('Login')}>
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
                marginTop: 40,
                marginBottom: 20,
              }}>
              <Text style={{color: COLORS.light, fontWeight: 'bold'}}>
                Have an account already? 
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