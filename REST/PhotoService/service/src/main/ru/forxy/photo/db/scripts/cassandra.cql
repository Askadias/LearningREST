create keyspace REST
    with placement_strategy = 'org.apache.cassandra.locator.SimpleStrategy'
    and strategy_options = {replication_factor:1};

use REST;

CREATE COLUMN FAMILY photo
WITH comparator = UTF8Type
AND key_validation_class = UTF8Type
AND default_validation_class = UTF8Type;
AND column_metadata = [
{column_name: url, validation_class: UTF8Type}
{column_name: last_name, validation_class: UTF8Type}
{column_name: url, validation_class: UTF8Type, index_type: }
{column_name: state, validation_class: UTF8Type}
{column_name: gender, validation_class: UTF8Type}
{column_name: birth_day, validation_class: LongType}
];

CREATE COLUMN FAMILY blog_entry
WITH comparator = TimeUUIDType
AND key_validation_class=UTF8Type
AND default_validation_class = UTF8Type;

CREATE COLUMN FAMILY page_view_counts
WITH default_validation_class=CounterColumnType
AND key_validation_class=UTF8Type AND comparator=UTF8Type;

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