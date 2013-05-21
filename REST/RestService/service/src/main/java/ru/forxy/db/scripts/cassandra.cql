create keyspace REST
    with placement_strategy = 'org.apache.cassandra.locator.SimpleStrategy'
    and strategy_options = {replication_factor:1};

use REST;
create column family User
with key_validation_class = 'UTF8Type'
and comparator = 'UTF8Type'
and default_validation_class = 'UTF8Type';

update column family User with
        column_metadata =
        [
        {column_name: first, validation_class: UTF8Type},
        {column_name: last, validation_class: UTF8Type},
        {column_name: age, validation_class: UTF8Type, index_type: KEYS}
        ];

assume User keys as utf8;

set User['ajenkins@mail.com']['first'] = 'Adam';
set User['ajenkins@mail.com']['last'] = 'Jenkins';
set User['ajenkins@mail.com']['age'] = '38';

set User['bmurray@mail.com']['first'] = 'Bob';
set User['bmurray@mail.com']['last'] = 'Murray';
set User['bmurray@mail.com']['age'] = '38';

set User['cblezinsky@mail.com']['first'] = 'Clif';
set User['cblezinsky@mail.com']['last'] = 'Blezinsky';
set User['cblezinsky@mail.com']['age'] = '38';