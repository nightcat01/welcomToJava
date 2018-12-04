import React, {Component, Fragment} from 'react';
import logo from './logo.svg';
import './App.css';
import MyName from './MyName';
import MyNameCompact from "./MyNameCompact";
import Counter from "./Counter";

class App extends Component {
    render() {
        const name = 'react'; // const는 한번 선언하고 바뀌지 않을 값
        let a = 'hello'; // let은 바꿔야 할 값
        // var의 경우는 거의 사용할 일이 없다고 함.

        const  value = 1;

        // style 작성
        const style = {
            backgroundColor: 'black',
            padding: '16px',
            color: 'white',
            fontSize: '12px'
        }
        return (
            <Fragment> {/*Fragment는 여러 태그들을 감싸는 에러를 내지 않기 위한 태그라고 생각하는게 편할 듯*/}
                <MyName />
                <MyNameCompact name='미쉘' />
                <Counter/>
                <div>
                    Hello
                </div>
                {/*<div>
                    Bye
                </div>*/}
                <div>
                    {
                        /*가능한한 JSX 내부에서는 로직을 사용하지 않는게 좋으며 어쩔 수 없을 경우에만 이와같이 작성해주면 된다.*/
                        1+1 === 3
                        ? (<div>맞는뎅</div>)
                        : (<div>틀린뎅</div>)
                    }
                    {
                        (function() {
                            if(value === 1) return (<div>하나</div>)
                            if(value === 2) return (<div>둘</div>)
                            if(value === 3) return (<div>셋</div>)
                        })()
                    }
                </div>
                <div style={style} // 태그 사이의 주석은 이렇게 단다고 한다.
                >hi there</div>
                {/*html에서의 class는 여기서 className으로 쓰인다.*/}
                <div className="App">
                    react
                </div>
            </Fragment>
        );
    }
}

export default App;
