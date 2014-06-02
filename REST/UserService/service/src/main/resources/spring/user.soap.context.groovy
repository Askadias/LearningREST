package spring

import ru.forxy.user.soap.UserServiceImpl

beans {
    xmlns jaxws: "http://cxf.apache.org/jaxws"

    userServiceImplSoap(UserServiceImpl) {
        userServiceFacade = ref(userServiceFacade)
    }

    jaxws.endpoint(id: 'userService.soap', address: '/soap/v1') {
        jaxws.implementor {
            ref(bean: 'userServiceImplSoap')
        }
    }
}
