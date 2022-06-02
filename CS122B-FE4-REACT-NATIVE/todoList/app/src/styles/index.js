import {StyleSheet} from 'react-native';
import COLORS from '../consts/color';

const STYLES = StyleSheet.create({
  inputContainer: {flexDirection: 'row', marginTop: 10},
  input: {
    color: COLORS.beige,
    paddingLeft: 30,
    borderBottomWidth: 1,
    borderColor: COLORS.orange,
    borderBottomWidth: 1,
    flex: 1,
    fontSize: 20,
  },
  inputIcon: {marginTop: 15, position: 'absolute'},
  btnPrimary: {
    backgroundColor: COLORS.orange,
    height: 30,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 25,
    marginBottom: 50,
  },
  secondaryPrimary: {
    backgroundColor: COLORS.darkPink,
    height: 30,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 25,
  },
  line: {height: 1, width: 30, backgroundColor: COLORS.beige},

});

export default STYLES;