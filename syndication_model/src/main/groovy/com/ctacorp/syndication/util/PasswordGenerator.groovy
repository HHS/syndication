package com.ctacorp.syndication.util

/**
 * Created by sgates on 10/10/14.
 */
class PasswordGenerator {
    static final UPPER = ('A'..'Z').toList()
    static final LOWER = ('a'..'z').toList()
    static final NUMBER = (0..9).toList()
    static final SYMBOL = ['!','@','#','$','%','^','&','*','(',')']

    static getRandomPassword(){
        Random ran = new Random()
        def letters = []
        3.times{
            letters << UPPER[ran.nextInt(UPPER.size())]
        }
        3.times{
            letters << LOWER[ran.nextInt(LOWER.size())]
        }
        3.times{
            letters << NUMBER[ran.nextInt(NUMBER.size())]
        }
        2.times{
            letters << SYMBOL[ran.nextInt(SYMBOL.size())]
        }
        Collections.shuffle(letters)
        letters.join('')
    }
}
