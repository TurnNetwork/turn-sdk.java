#define TESTNET
#include <bubble/bubble.hpp>
#include <string>
using namespace bubble;


CONTRACT ContractEmitEvent : public bubble::Contract{
   public:
      PLATON_EVENT0(transfer,std::string,std::string,Address)

      ACTION void init(){}

      ACTION void setNameAndEmitAddress(std::string name, Address address){
           stringstorage.self() = name;
           PLATON_EMIT_EVENT0(transfer,name,name,address);
      }

      CONST std::string getName(){
          return stringstorage.self();
      }

      CONST Address getAddress(){
          return bubble_caller();
      }

   private:
      bubble::StorageType<"sstorage"_n, std::string> stringstorage;
};

PLATON_DISPATCH(ContractEmitEvent, (init)(setNameAndEmitAddress)(getName)(getAddress))
