import React from 'react'
import {View, Text} from "react-native";
import { SafeAreaView } from 'react-native-safe-area-context';
import COLORS from '../../consts/color';

function Login() {
    return(
        <SafeAreaView
            style = {{paddingHorizontal:20, flex: 1, backgroundColor:COLORS.white}}>
                <ScrollView showsVerticalScrollIndicator={false}> </ScrollView>
        </SafeAreaView>
        );
}

export default Login;