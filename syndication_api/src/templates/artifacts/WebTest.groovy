
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


class @webtest.name.caps@Test extends grails.util.WebTest {

    // Unlike unit tests, functional tests are often sequence dependent.
    // Specify that sequence here.
    void suite() {
        test@webtest.name.caps@ListNewDelete()
        // add tests for more operations here
    }

    def test@webtest.name.caps@ListNewDelete() {
        webtest('@webtest.name.caps@ basic operations: view list, create new entry, view, edit, delete, view') {
            invoke(url:'@webtest.name.lower@')
            verifyText(text:'Home')

            verifyListPage(0)

            clickLink(label:'New @webtest.name.caps@')
            verifyText(text:'Create @webtest.name.caps@')
            clickButton(label:'Create')
            verifyText(text:'Show @webtest.name.caps@', description:'Detail page')
            clickLink(label:'List', description:'Back to list view')

            verifyListPage(1)

            group(description:'edit the one element') {
                clickLink(label:'Show', description:'go to detail view')
                clickButton(label:'Edit')
                verifyText(text:'Edit @webtest.name.caps@')
                clickButton(label:'Update')
                verifyText(text:'Show @webtest.name.caps@')
                clickLink(label:'List', description:'Back to list view')
            }

            verifyListPage(1)

            group(description:'delete the only element') {
                clickLink(label:'Show', description:'go to detail view')
                clickButton(label:'Delete')
                verifyXPath(xpath:"//div[@class='message']", text:/@webtest.name.caps@.*deleted./, regex:true)
            }

            verifyListPage(0)
        }
    }

    String ROW_COUNT_XPATH = "count(//td[@class='actionButtons']/..)"

    def verifyListPage(int count) {
        ant.group(description:"verify @webtest.name.caps@ list view with $count row(s)") {
            verifyText(text:'@webtest.name.caps@ List')
            verifyXPath(xpath:ROW_COUNT_XPATH, text:count, description:"$count row(s) of data expected")
        }
    }
}