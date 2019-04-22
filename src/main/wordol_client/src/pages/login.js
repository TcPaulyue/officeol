import React from 'react';
import {Icon,Button,Input,Form} from 'antd';
import styles from './login.css';
import router from 'umi/router';
class Login extends React.Component{
    handleSubmit = (e)=>{
        e.preventDefault();
        this.props.form.validateFields((err,values)=>{
            if(!err){
                console.log('Received values of form: ',values);
                let newRout = '/editor?'+'userId='+values.userId+"&fileId="+values.fileId;
                router.push(newRout);
            }
        })
    };

    render(){
        const {getFieldDecorator} = this.props.form;
        return(
            <div>
                <div className={styles.title}>
                    <h2 style={{fontSize:'18px',color:'#000000'}}>在线word</h2>
                </div>
                <div className={styles.login_form}>
                    <Form onSubmit={this.handleSubmit}>
                        <Form.Item>
                            {getFieldDecorator('userId',{
                                rules:[{required:true,message:'请输入用户名!'}]
                            })(
                                <span>
                                    <Input placeholder="用户名"
                                        prefix={<Icon type="user"></Icon>}
                                    />
                                </span>
                            )}
                        </Form.Item>
                        <Form.Item>
                            {getFieldDecorator('fileId',{
                                rules:[{required:true,message:'请输入文档Id!'}],
                            })(
                                <Input
                                    placeholder="文档Id"
                                    prefix={<Icon type="lock"/>}
                                ></Input>
                            )}
                        </Form.Item>
                        <Form.Item>
                            <Button type="primary" htmlType="submit" style={{width:'100%'}}>打开文档</Button>
                        </Form.Item>
                    </Form>
                </div>
            </div>
        )
    }
}
export default Form.create({name:'normal_login'})(Login);
