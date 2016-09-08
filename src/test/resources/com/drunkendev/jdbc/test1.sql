--
-- test1.sql.sql    Sep 8 2016, 17:22
--
-- Copyright 2016 Drunken Dev.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--      http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
-- Author: Brett Ryan
--

select * from some_table;

select *
  from some_table
 where x = 'test'
   and a > 10/3;

select a
      ,b
--       ,c
      ,d
  from table1;

select * from a-b-c;

select * from abc 'a--b--c';

select * from [a--b--c];
