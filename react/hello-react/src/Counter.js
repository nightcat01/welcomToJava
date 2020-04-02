import React, { Component } from 'react';

const Problematic = () => {
    throw (new Error('버그가 나타났다!'));
    return (
        <div>

        </div>
    );
};

class Counter extends Component {
    state = {
        number : 0
    }

    constructor(props) {
        super(props); // 이미 생성 된 class를 한번 덮어씌운다는 의미로 해석하면 되는 듯
        console.log('constructor');
    }

    componentWillMount() { // 화면에 나가기 전에 호출 되는 API 현재 버전에서는 거의 사용되지 않음.
        console.log('componentWillMount (deprecated)');
    }

    componentDidMount() { // 컴포넌트가 화면에 나타난 뒤에 호출 되는 함수
        // 외부 라이브러리 연동: D3, masonry, etc
        // 컴포넌트에서 필요한 데이터 요청: Ajax, GraphQL, etc
        // DOM 에 관련된 작업: 스크롤 설정, 크기 읽어오기 등
        console.log('componentDidMount');
    }

    // componentWillReceiveProps(nextProps) { // 컴포넌트가 새로운 props를 받게 될 때 호출 되는 함수
        // 주로 state가 props에 따라 변해야 할 때 사용
        // this.props 는 아직 바뀌지 않은 상태
    // }

    // static getDerivedStateFromProps(nextProps, prevState) {
        // 여기서는 setState 를 하는 것이 아니라
        // 특정 props 가 바뀔 때 설정하고 설정하고 싶은 state 값을 리턴하는 형태로
        // 사용됩니다.
        /*
        if (nextProps.value !== prevState.value) {
          return { value: nextProps.value };
        }
        return null; // null 을 리턴하면 따로 업데이트 할 것은 없다라는 의미
        */
    // }

    shouldComponentUpdate(nextProps, nextState) {
        // return false 하면 업데이트를 안함 기본은 true
        // return this.props.checked !== nextProps.checked
        // 5 의 배수라면 리렌더링 하지 않음
        console.log('shouldComponentUpdate');
        if (nextState.number % 5 === 0) return false;
        return true;
    }

    componentWillUpdate(nextProps, nextState) { // shouldComponentUpdate가 true를 return 했을 때만 동작
        // 주로 애니메이션 효과 초기화, 이벤트 리스너 제거 작업을 한다.
        console.log('componentWillUpdate');
    }

    componentDidUpdate(prevProps, prevState) { // render()를 호출하고 난 뒤에 동작
        // 함수 호출 시에는 this.props, this.state가 변경되어 있는 시점
        // 파라미터를 통해 이전 값인 prevProps, prevState를 조회 가능하다.
        console.log('componentDidUpdate');
    }

    // componentWillUnmount() {
        // 이벤트, setTimeout, 외부 라이브러리 인스턴스 제거
    // }

    componentDidCatch(error, info) { // 자식 컴포넌트에서 일어나는 모든 에러를 잡는 함수
        // 자신의 render 함수에서 발생하는 것은 잡아낼 수 없다
        this.setState({
            error: true
        });
    }

    // handleIncrease = () => {
    //     this.setState({
    //         number: this.state.number + 1
    //     });
    // }
    //
    // handleDecrease = () => {
    //     this.setState({
    //         number: this.state.number - 1
    //     });
    // }

    handleIncrease = () => {
        const { number } = this.state;
        this.setState({
            number: number + 1
        });
    }

    handleDecrease = () => {
        this.setState(
            ({ number }) => ({
                number: number - 1
            })
        );
    }

    render() {
        if (this.state.error) return (<h1>에러발생!</h1>);
        return (
            <div>
                <h1>카운터</h1>
                <div>값: {this.state.number}</div>
                { this.state.number === 4 && <Problematic /> }
                <button onClick={this.handleIncrease}>+</button>
                <button onClick={this.handleDecrease}>-</button>
            </div>
        );
    }
}

export  default Counter;